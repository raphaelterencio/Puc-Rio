# run_i_to_Z.py
from map_loader import load_map, terrain_cost
from astar import astar

MAP_PATH = "mapa.txt" 

if __name__ == "__main__":
    m = load_map(MAP_PATH)
    dist, path = astar(m, m["start"], m["goal"], terrain_cost)
    if dist == float("inf"):
        print("Sem caminho i->Z.")
    else:
        print("Custo i->Z:", int(dist))          # custo total para entrar em Z
        print("Passos:", max(0, len(path)-1))    # movimentações (sem diagonais)
        print("Início:", path[:5])
        print("Fim:", path[-5:])
