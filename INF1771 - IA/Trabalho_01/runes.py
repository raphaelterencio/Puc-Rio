from config import RUNES, EVENT_DIFFICULTY


def _subset_usage_and_power():
    n = len(RUNES)
    subsets = []
    for mask in range(1, 1 << n):  # ignora vazio
        uses = [0]*n
        pwr = 0.0
        for i in range(n):
            if mask & (1 << i):
                uses[i] = 1            # cada runa no máx. 1 vez por evento
                pwr += float(RUNES[i]["power"])
        subsets.append((uses, pwr))
    return subsets

_SUBSETS = _subset_usage_and_power()  # cache

def _dp_for_keep_idx(events_labels, keep_idx):
    """
    keep_idx: índice da runa que ficará "inteira" (cap=4 em vez de 5).
    Retorna (total_time, details, rune_usage) ótimo para esse keep_idx.
    """
    # capacidades por runa
    caps = [5, 5, 5, 5, 5]
    caps[keep_idx] = 4  # força a “runa inteira” (<=4 usos)

    # dificuldades por evento, na ordem dada
    Ds = [float(EVENT_DIFFICULTY[lbl]) for lbl in events_labels]
    n_ev = len(Ds)
    n_r = len(RUNES)

    # DP por estados de uso (u0..u4). Estados: 0..cap
    start_state = (0, 0, 0, 0, 0)
    dp = {start_state: 0.0}
    parent = {}  

    for ev_idx in range(n_ev):
        D = Ds[ev_idx]
        next_dp = {}
        next_parent = {}

        for state, best_time in dp.items():
            # tenta todos os subconjuntos não vazios
            for uses_vec, pwr_sum in _SUBSETS:
                # checa capacidade
                ok = True
                new_state = [0]*n_r
                for i in range(n_r):
                    u = state[i] + uses_vec[i]
                    if u > caps[i]:
                        ok = False
                        break
                    new_state[i] = u
                if not ok:
                    continue

                # custo desse evento com esse subset
                t_ev = D / pwr_sum
                cand_time = best_time + t_ev
                new_state_t = tuple(new_state)

                old = next_dp.get(new_state_t)
                if (old is None) or (cand_time < old):
                    next_dp[new_state_t] = cand_time
                    next_parent[(ev_idx+1, new_state_t)] = (state, uses_vec)

        dp = next_dp
        parent.update(next_parent)

    best_total = None
    best_state = None
    for st, t in dp.items():
        if (best_total is None) or (t < best_total):
            best_total = t
            best_state = st

    # reconstrói alocações por evento
    details = []
    st = best_state
    for ev_idx in range(n_ev, 0, -1):
        prev_st, uses_vec = parent[(ev_idx, st)]
        # monta nomes de runas usadas neste evento
        ev_runes = []
        P = 0.0
        for i in range(n_r):
            if uses_vec[i] == 1:
                ev_runes.append(RUNES[i]["name"])
                P += float(RUNES[i]["power"])
        D = Ds[ev_idx-1]
        t = D / P
        details.append({
            "label": events_labels[ev_idx-1],
            "D": D,
            "P": P,
            "time": t,
            "runes": ev_runes
        })
        st = prev_st
    details.reverse()

    # contagem de usos por runa
    rune_usage = {r["name"]: 0 for r in RUNES}
    for d in details:
        for nm in d["runes"]:
            rune_usage[nm] += 1

    return best_total, details, rune_usage

def allocate_runes_optimal(event_labels_in_order):
    best = None
    best_payload = None
    for keep_idx in range(len(RUNES)):
        total, details, usage = _dp_for_keep_idx(event_labels_in_order, keep_idx)
        if (best is None) or (total < best):
            best = total
            best_payload = (details, usage)

    usages = sorted(best_payload[1].values())
    keep_rule_ok = (usages == [4,5,5,5,5])

    return {
        "total_event_time": best,
        "details": best_payload[0],
        "rune_usage": best_payload[1],
        "keep_rule_ok": keep_rule_ok,
    }

allocate_runes_greedy = allocate_runes_optimal
