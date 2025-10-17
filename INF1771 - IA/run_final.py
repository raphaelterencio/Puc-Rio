# run_final.py
from map_loader import load_map, terrain_cost
from pairwise import build_pairwise
from tsp_held_karp import solve_tsp_path
from runes import allocate_runes_greedy

def main():
    MAP_PATH = "mapa.txt"  # troque se quiser
    m = load_map(MAP_PATH)

    # 1) distâncias entre i, 16 eventos, Z
    data = build_pairwise(m)
    labels, dist = data["labels"], data["dist"]

    # 2) TSP caminho (start=i, end=Z)
    travel_cost, order = solve_tsp_path(labels, dist)
    if travel_cost == float("inf"):
        print("Não existe rota i->eventos->Z no mapa.")
        return

    # 3) alocação de runas e tempo de eventos
    events_order = [x for x in order if x not in ("i", "Z")]
    alloc = allocate_runes_greedy(events_order)
    events_time = alloc["total_event_time"]
    final_cost = travel_cost + events_time

    # ----- Saída final (curta, direta) -----
    print("=== Resultado Final ===")
    print("Ordem:", " -> ".join(order))
    print("Custo de viagem (A*):", int(travel_cost))
    print("Tempo dos eventos:", f"{events_time:.2f}")
    print("CUSTO TOTAL:", f"{final_cost:.2f}")

    print("\n-- Uso de Runas --")
    for name, used in alloc["rune_usage"].items():
        print(f"{name}: {used}")
    print("Regra '1 runa inteira' ok?:", alloc["keep_rule_ok"])

    print("\n-- Eventos (D, P, tempo, runas) --")
    for d in alloc["details"]:
        print(f"{d['label']}: D={int(d['D'])}  P={d['P']:.1f}  t={d['time']:.2f}  {', '.join(d['runes'])}")

if __name__ == "__main__":
    main()
