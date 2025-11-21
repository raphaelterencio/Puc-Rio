import heapq
import math
import networkx as nx


def dijkstra(G: nx.DiGraph, origem: int, destino: int) -> tuple[list, float]:
    """
    Encontra o caminho de menor peso entre dois vértices em um grafo orientado
    usando o algoritmo de Dijkstra.

    Parâmetros:
        G (nx.DiGraph): Grafo orientado com pesos não negativos nas arestas.
        origem: vértice de origem.
        destino: vértice de destino.

    Retorna:
        tuple[list, float]: (caminho, peso_total)
            - caminho: lista de vértices do caminho de menor peso de origem até destino.
            - peso_total: soma dos pesos das arestas do caminho.
          Se não existir caminho entre origem e destino, retorna ([], float('inf')).

    Observação:
        O algoritmo assume que todas as arestas possuem peso não negativo.
    """

    dist = {v: math.inf for v in G.nodes()}

    if origem not in dist or destino not in dist:
        return [], float('inf')

    dist[origem] = 0.0

    anterior = {}

    heap = [(0.0, origem)]

    while heap:
        dist_u, u = heapq.heappop(heap)

        if dist_u > dist[u]:
            continue

        if u == destino:
            break

        for v in G.neighbors(u):
            peso = G[u][v].get("weight", 1.0)

            if peso < 0:
                raise ValueError(
                    f"Aresta ({u}, {v}) possui peso negativo ({peso}). "
                    "Dijkstra exige pesos não negativos."
                )

            nova_dist = dist_u + peso

            
            if nova_dist < dist[v]:
                dist[v] = nova_dist
                anterior[v] = u
                heapq.heappush(heap, (nova_dist, v))

    
    if dist[destino] == math.inf:
        return [], float('inf')

    caminho = []
    atual = destino

    while True:
        caminho.append(atual)
        if atual == origem:
            break
        if atual not in anterior:
            
            return [], float('inf')
        atual = anterior[atual]

    caminho.reverse()
    return caminho, dist[destino]


if __name__ == "__main__":
    # Exemplo de teste 1: grafo com caminho mais curto (do enunciado)
    G = nx.DiGraph()
    G.add_edge('s', 'a', weight=1)
    G.add_edge('s', 'b', weight=4)
    G.add_edge('a', 'b', weight=2)
    G.add_edge('a', 't', weight=6)
    G.add_edge('b', 't', weight=1)

    caminho, custo = dijkstra(G, 's', 't')
    print("Grafo 1:")
    print("Caminho mínimo de 's' até 't':", caminho)
    print("Custo total:", custo)
    # Esperado: (['s', 'a', 'b', 't'], 4.0)

    print()

    # Exemplo de teste 2: caso sem caminho (do enunciado)
    G2 = nx.DiGraph()
    G2.add_edge('a', 'b', weight=1)
    G2.add_edge('c', 'd', weight=2)

    caminho2, custo2 = dijkstra(G2, 'a', 'd')
    print("Grafo 2:")
    print("Caminho mínimo de 'a' até 'd':", caminho2)
    print("Custo total:", custo2)
    # Esperado: ([], inf)
