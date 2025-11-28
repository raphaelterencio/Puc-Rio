# run_total.py
from map_loader import load_map, terrain_cost
from pairwise import build_pairwise
from tsp_held_karp import solve_tsp_path
from runes import allocate_runes_greedy

MAP_PATH = "mapa.txt"

if __name__ == "__main__":
    m = load_map(MAP_PATH)

    # distâncias entre i, eventos, Z
    data = build_pairwise(m)
    labels = data["labels"]
    dist = data["dist"]

    # ordem ótima de viagem (i -> ...eventos... -> Z)
    travel_cost, order = solve_tsp_path(labels, dist)
    if travel_cost == float('inf') or not order:
        print("Não existe rota viável que visita todos os eventos e chega a Z.")
        raise SystemExit(0)

    # extrai só os 16 eventos na ordem
    events_order = [x for x in order if x not in ('i', 'Z')]

    # alocação de runas + tempo de eventos
    alloc = allocate_runes_greedy(events_order)
    events_time = alloc["total_event_time"]

    # custo final
    final_cost = travel_cost + events_time

    print("== Resultado Final ==")
    print("Ordem:", " -> ".join(order))
    print("Custo de viagem (A*):", int(travel_cost))
    print("Tempo dos eventos:", f"{events_time:.2f}")
    print("Custo FINAL:", f"{final_cost:.2f}")

    print("\n-- Uso de Runas (total de participações) --")
    for name, used in alloc["rune_usage"].items():
        print(f"{name}: {used}")
    print("Regra 'manter 1 runa inteira' ok?:", alloc["keep_rule_ok"])

    print("\n-- Detalhe por evento --")
    for d in alloc["details"]:
        rl = ", ".join(d["runes"])
        print(f"Evento {d['label']}: D={int(d['D'])}  P={d['P']:.1f}  tempo={d['time']:.2f}  runas=[{rl}]")
