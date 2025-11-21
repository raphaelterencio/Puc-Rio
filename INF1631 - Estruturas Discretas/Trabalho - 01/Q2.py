import networkx as nx

def agm_remocao_ciclos(G: nx.Graph) -> tuple[list, float]:
    """
    Encontra uma árvore geradora de custo mínimo removendo arestas
    de maior peso em ciclos (algoritmo visto em aula).
    
    Parâmetros:
        G (nx.Graph): Grafo conexo não orientado com pesos nas arestas
    
    Retorna:
        tuple[list, float]: (lista de arestas da AGM, custo total)
                            Cada aresta é uma tupla (u, v, peso)
    """

    
    H = G.copy()

    
    arestas = []
    for u, v, data in G.edges(data=True):
        peso = data.get("weight", 1)
        arestas.append((u, v, peso))

    # Ordena as arestas por peso DECRESCENTE
    arestas.sort(key=lambda x: x[2], reverse=True)

    
    for u, v, peso in arestas:
        # remove temporariamente
        H.remove_edge(u, v)

        if not nx.is_connected(H):
            
            H.add_edge(u, v, weight=peso)

    
    agm_arestas = []
    for u, v, data in H.edges(data=True):
        peso = data.get("weight", 1)
        agm_arestas.append((u, v, peso))

    # Custo total da AGM
    custo_total = float(sum(peso for _, _, peso in agm_arestas))

    return agm_arestas, custo_total


if __name__ == "__main__":

    # Grafo com 4 vértices
    G = nx.Graph()
    G.add_edge(1, 2, weight=4)
    G.add_edge(2, 3, weight=2)
    G.add_edge(3, 4, weight=3)
    G.add_edge(4, 1, weight=5)
    G.add_edge(1, 3, weight=7)

    agm, custo = agm_remocao_ciclos(G)
    print("AGM (arestas):", agm)
    print("Custo total:", custo)
    # Saída esperada: ([(2, 3, 2), (3, 4, 3), (1, 2, 4)], 9.0)
    # (a ordem das arestas pode ser diferente, mas o custo deve ser 9.0)
