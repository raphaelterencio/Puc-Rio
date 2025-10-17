# astar.py — A* 4 direções, custo ao ENTRAR, heurística Manhattan×1
import heapq

def _manhattan(a, b):
    return abs(a[0]-b[0]) + abs(a[1]-b[1])

def _in_bounds(grid, r, c):
    return 0 <= r < len(grid) and 0 <= c < len(grid[r])

def astar(mapdata, src, dst, terrain_cost_func):
    grid = mapdata["grid"]
    if src is None or dst is None:
        return float("inf"), []

    start, goal = src, dst
    g = {start: 0.0}
    parent = {start: None}
    pq = [(0.0, start)]
    closed = set()

    while pq:
        f, u = heapq.heappop(pq)
        if u in closed:
            continue
        closed.add(u)

        if u == goal:
            # reconstrói caminho
            path = []
            x = u
            while x is not None:
                path.append(x)
                x = parent[x]
            path.reverse()
            return g[u], path

        r, c = u
        for dr, dc in ((-1,0), (1,0), (0,-1), (0,1)):  # sem diagonais
            vr, vc = r+dr, c+dc
            if not _in_bounds(grid, vr, vc):
                continue
            ch = grid[vr][vc]
            cost = terrain_cost_func(ch)
            if cost is None:               # bloqueado ('#' ou inválido)
                continue
            ng = g[u] + float(cost)        # custo ao ENTRAR no vizinho
            if (vr, vc) not in g or ng < g[(vr, vc)]:
                g[(vr, vc)] = ng
                parent[(vr, vc)] = u
                h = _manhattan((vr, vc), goal) * 1.0
                heapq.heappush(pq, (ng + h, (vr, vc)))

    return float("inf"), []



# --- depuração/visualização ---
import heapq

def astar_debug(mapdata, src, dst, terrain_cost_func, print_every=2000, print_full=False):
    """
    Igual ao A*, mas guarda fronteira (open) e visitados (closed) para visualização.
    print_every: printa contagens a cada N expansões.
    print_full:  se True, imprime o mapa final com overlay.
    Retorna (dist, path, opened_set, closed_set).
    """
    grid = mapdata["grid"]
    if src is None or dst is None:
        return float("inf"), [], set(), set()

    def _manhattan(a, b): return abs(a[0]-b[0]) + abs(a[1]-b[1])
    def _in_bounds(r, c): return 0 <= r < len(grid) and 0 <= c < len(grid[r])

    start, goal = src, dst
    g = {start: 0.0}
    parent = {start: None}
    pq = [(0.0, start)]
    opened = set([start])
    closed = set()
    steps = 0

    while pq:
        f, u = heapq.heappop(pq)
        if u in closed:
            continue
        closed.add(u)

        steps += 1
        if steps % max(1, print_every) == 0:
            print(f"[A*] expandidos={len(closed)}  fronteira={len(opened - closed)}")

        if u == goal:
            path = []
            x = u
            while x is not None:
                path.append(x)
                x = parent[x]
            path.reverse()

            if print_full:
                _print_overlay(grid, opened, closed, path, start, goal)
            return g[u], path, opened, closed

        r, c = u
        for dr, dc in ((-1,0),(1,0),(0,-1),(0,1)):
            vr, vc = r+dr, c+dc
            if not _in_bounds(vr, vc):
                continue
            ch = grid[vr][vc]
            cost = terrain_cost_func(ch)
            if cost is None:
                continue
            ng = g[u] + float(cost)
            if (vr, vc) not in g or ng < g[(vr, vc)]:
                g[(vr, vc)] = ng
                parent[(vr, vc)] = u
                h = _manhattan((vr, vc), goal)
                heapq.heappush(pq, (ng + h, (vr, vc)))
                opened.add((vr, vc))

    if print_full:
        _print_overlay(grid, opened, closed, [], start, goal)
    return float("inf"), [], opened, closed

def _print_overlay(grid, opened, closed, path, start, goal):
    # desenha um snapshot simples no console
    mark = {}
    for r,c in closed: mark[(r,c)] = 'x'     # visitados
    for r,c in opened:
        if (r,c) not in mark: mark[(r,c)] = 'o'  # fronteira
    for r,c in path: mark[(r,c)] = '*'       # caminho final
    mark[start] = 'i'
    mark[goal]  = 'Z'

    for r in range(len(grid)):
        row_chars = []
        for c in range(len(grid[r])):
            row_chars.append(mark.get((r,c), grid[r][c]))
        print("".join(row_chars))
