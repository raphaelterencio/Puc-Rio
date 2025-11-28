import heapq
from map_loader import terrain_cost

def get_pois(mapdata):
    labels = ['i'] + sorted(mapdata['events'].keys()) + ['Z']
    coords = []
    for lbl in labels:
        if lbl == 'i': coords.append(mapdata['start'])
        elif lbl == 'Z': coords.append(mapdata['goal'])
        else: coords.append(mapdata['events'][lbl])
    return labels, coords

def _manhattan(a,b): return abs(a[0]-b[0]) + abs(a[1]-b[1])

def _in_bounds(grid, r, c):
    return 0 <= r < len(grid) and 0 <= c < len(grid[r])

def astar_multitarget(mapdata, src, goals, terrain_cost_func):
    """Um único A* encontrando distâncias para vários alvos; para quando achar todos."""
    grid = mapdata["grid"]
    remaining = set(goals)
    dist_found = {}

    g = {src: 0.0}
    parent = {src: None}

    def h_min(v):
        if not remaining: return 0
        return min(_manhattan(v, gk) for gk in remaining)

    pq = [(h_min(src), src)]
    closed = set()

    while pq and remaining:
        f, u = heapq.heappop(pq)
        if u in closed: continue
        closed.add(u)

        if u in remaining:
            dist_found[u] = g[u]
            remaining.remove(u)
            if not remaining: break 

        r, c = u
        for dr, dc in ((-1,0),(1,0),(0,-1),(0,1)):
            vr, vc = r+dr, c+dc
            if not _in_bounds(grid, vr, vc): continue
            ch = grid[vr][vc]
            cost = terrain_cost_func(ch)
            if cost is None: continue
            ng = g[u] + float(cost)
            if (vr, vc) not in g or ng < g[(vr, vc)]:
                g[(vr, vc)] = ng
                parent[(vr, vc)] = u
                heapq.heappush(pq, (ng + h_min((vr, vc)), (vr, vc)))

    return dist_found  # mapeia coord->custo

def build_pairwise(mapdata):
    labels, coords = get_pois(mapdata)
    n = len(coords)
    dist = [[float('inf')]*n for _ in range(n)]
    paths = {}  

    for i in range(n):
        goals = [coords[j] for j in range(n) if j != i]
        found = astar_multitarget(mapdata, coords[i], goals, terrain_cost)
        # preencher a linha i
        for j in range(n):
            if i == j:
                dist[i][j] = 0
            else:
                d = found.get(coords[j])
                if d is not None:
                    dist[i][j] = int(d)
    return {"labels": labels, "coords": coords, "dist": dist, "paths": paths}
