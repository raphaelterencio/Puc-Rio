from map_loader import load_map, terrain_cost
from astar import astar_debug

MAP_PATH = "mapa.txt"   # use o do prof
SRC_LABEL = 'i'         # origem
DST_LABEL = 'Z'         # destino (troque se quiser visualizar outro par)

if __name__ == "__main__":
    m = load_map(MAP_PATH)

    # escolhe coords
    if SRC_LABEL == 'i': src = m["start"]
    elif SRC_LABEL == 'Z': src = m["goal"]
    else: src = m["events"][SRC_LABEL]

    if DST_LABEL == 'i': dst = m["start"]
    elif DST_LABEL == 'Z': dst = m["goal"]
    else: dst = m["events"][DST_LABEL]

    # imprime contagens a cada 2000 expansões; printa overlay final
    dist, path, opened, closed = astar_debug(m, src, dst, terrain_cost,
                                             print_every=2000, print_full=False)

    if dist == float("inf"):
        print("Sem caminho.")
    else:
        print(f"Custo: {int(dist)}  Passos: {max(0, len(path)-1)}")
        print(f"Visitados: {len(closed)}  Fronteira: {len(opened - closed)}")

        # Se quiser imprimir o mapa com overlay, ligue isto (cuidado: 137x320 no console):
        # from astar import _print_overlay
        # _print_overlay(m['grid'], opened, closed, path, src, dst)
