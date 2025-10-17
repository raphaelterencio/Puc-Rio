# tsp_held_karp.py
def solve_tsp_path(labels, dist):
    """
    labels: ['i', <16 eventos ordenados>, 'Z']
    dist: matriz NxN com custos inteiros (dist[i][j])
    Retorna (custo_total, ordem_labels)
    """
    n_total = len(labels)           # deve ser 18
    n_ev = n_total - 2              # 16 eventos
    if n_ev <= 0:
        return 0, labels[:]         # nada a fazer

    # índices úteis
    I = 0                           # 'i'
    Z = n_total - 1                 # 'Z'
    # eventos são 1..16 no labels; mapeio para 0..15 no bitmask
    # evento k (0..15) -> índice real no labels = 1 + k

    # checagens básicas (se algo for inf, rota é impossível)
    for k in range(n_ev):
        if dist[I][1+k] == float('inf') or dist[1+k][Z] == float('inf'):
            return float('inf'), []

    # DP[mask][j]: menor custo saindo de i, visitando 'mask' (eventos),
    # terminando no evento j (j é 0..15)
    INF = float('inf')
    size = 1 << n_ev
    dp = [ [INF]*n_ev for _ in range(size) ]
    parent = [ [-1]*n_ev for _ in range(size) ]

    # base: escolher primeiro evento j
    for j in range(n_ev):
        dp[1<<j][j] = dist[I][1+j]   # i -> evento j

    # transições
    for mask in range(size):
        # para cada último evento j presente em mask
        for j in range(n_ev):
            if not (mask & (1<<j)): 
                continue
            prev_mask = mask ^ (1<<j)
            if prev_mask == 0:
                continue
            best = dp[mask][j]
            # tenta vir de k -> j
            for k in range(n_ev):
                if not (prev_mask & (1<<k)):
                    continue
                c_prev = dp[prev_mask][k]
                if c_prev == INF:
                    continue
                c = c_prev + dist[1+k][1+j]
                if c < best:
                    best = c
                    parent[mask][j] = k
            dp[mask][j] = best

    # fechar em Z
    full = size - 1
    best_cost = INF
    last_ev = -1
    for j in range(n_ev):
        if dp[full][j] == INF or dist[1+j][Z] == INF:
            continue
        c = dp[full][j] + dist[1+j][Z]
        if c < best_cost:
            best_cost = c
            last_ev = j

    if best_cost == INF:
        return INF, []

    # reconstrução da ordem: i -> ...eventos... -> Z
    ordem_idx = []
    mask = full
    j = last_ev
    while j != -1:
        ordem_idx.append(1 + j)   # índice no labels
        pj = parent[mask][j]
        mask ^= (1<<j)
        j = pj
    ordem_idx.reverse()

    ordem_labels = ['i'] + [labels[idx] for idx in ordem_idx] + ['Z']
    return int(best_cost), ordem_labels
