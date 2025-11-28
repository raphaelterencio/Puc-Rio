# map_loader.py
from config import TERRAIN_COST, EVENT_CHARS

def _norm(ch):
    if ch == 'I': return 'i'
    if ch == 'f': return 'F'
    return ch

def load_map(path):
    with open(path, "r", encoding="utf-8") as f:
        lines = f.read().splitlines()

    grid = [list("".join(_norm(ch) for ch in ln)) for ln in lines]

    start = None
    goal = None
    events = {}

    for r, row in enumerate(grid):
        for c, ch in enumerate(row):
            if ch == 'i' and start is None:
                start = (r, c)
            elif ch == 'Z' and goal is None:
                goal = (r, c)
            elif ch in EVENT_CHARS and ch not in events:
                events[ch] = (r, c)

    return {"grid": grid, "start": start, "goal": goal, "events": events}

def terrain_cost(ch):

    ch = _norm(ch)
    if ch in EVENT_CHARS:
        return 1
    return TERRAIN_COST.get(ch, None) 
