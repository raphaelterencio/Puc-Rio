# run_pairwise.py
from map_loader import load_map
from pairwise import build_pairwise, get_pois

MAP_PATH = "mapa.txt"  

if __name__ == "__main__":
    m = load_map(MAP_PATH)
    data = build_pairwise(m)

    labels = data["labels"]
    dist = data["dist"]

    print("POIs (ordem):", labels)
    print("Total de POIs:", len(labels))
    # exemplos rápidos
    print("Dist(i -> Z):", dist[0][-1])

    # distâncias de i para cada evento
    for k in range(1, len(labels)-1):
        print("Dist(i -> %s): %s" % (labels[k], dist[0][k]))
