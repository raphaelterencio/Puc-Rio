# run_tsp.py
from map_loader import load_map
from pairwise import build_pairwise
from tsp_held_karp import solve_tsp_path

MAP_PATH = "mapa.txt"  # seu arquivo do professor

if __name__ == "__main__":
    m = load_map(MAP_PATH)
    data = build_pairwise(m)
    labels = data["labels"]
    dist = data["dist"]

    cost, order = solve_tsp_path(labels, dist)
    if cost == float('inf') or not order:
        print("Não existe rota que visite todos os eventos e chegue a Z.")
    else:
        print("Custo de viagem ótimo (i -> eventos -> Z):", cost)
        print("Ordem:", " -> ".join(order))
