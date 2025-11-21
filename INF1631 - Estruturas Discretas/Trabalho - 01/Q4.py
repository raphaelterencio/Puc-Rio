import time
import random
import networkx as nx

from Q2 import agm_remocao_ciclos


def comparar_agms(G: nx.Graph) -> dict:
    """
    Compara a performance de três algoritmos de AGM medindo tempo de execução.

    Algoritmos comparados:
        - remocao_ciclos: algoritmo da Questão 2 (remoção das arestas mais caras em ciclos)
        - prim: algoritmo de Prim (via networkx.minimum_spanning_tree)
        - kruskal: algoritmo de Kruskal (via networkx.minimum_spanning_tree)

    Parâmetros:
        G (nx.Graph): Grafo conexo não orientado com pesos nas arestas.

    Retorna:
        dict: Dicionário com tempos de execução (em segundos) e custos:
              {
                  'remocao_ciclos': {'tempo': float, 'custo': float},
                  'prim':           {'tempo': float, 'custo': float},
                  'kruskal':        {'tempo': float, 'custo': float}
              }
    """

    resultados = {}

    # Algoritmo da Questão 2 (remoção de arestas em ciclos)
    inicio = time.time()
    arestas, custo = agm_remocao_ciclos(G)
    fim = time.time()
    resultados['remocao_ciclos'] = {
        'tempo': fim - inicio,
        'custo': custo
    }

    # Prim (via NetworkX)
    inicio = time.time()
    T_prim = nx.minimum_spanning_tree(G, algorithm="prim", weight="weight")
    fim = time.time()
    custo_prim = float(
        sum(data.get("weight", 1) for _, _, data in T_prim.edges(data=True))
    )
    resultados['prim'] = {
        'tempo': fim - inicio,
        'custo': custo_prim
    }

    # Kruskal (via NetworkX)
    inicio = time.time()
    T_kruskal = nx.minimum_spanning_tree(G, algorithm="kruskal", weight="weight")
    fim = time.time()
    custo_kruskal = float(
        sum(data.get("weight", 1) for _, _, data in T_kruskal.edges(data=True))
    )
    resultados['kruskal'] = {
        'tempo': fim - inicio,
        'custo': custo_kruskal
    }

    return resultados


def gerar_grafo_teste(n_vertices: int, densidade: float = 0.3) -> nx.Graph:
    """
    Gera grafo aleatório não orientado e conexo, com pesos inteiros nas arestas,
    para teste de performance dos algoritmos de AGM.

    Parâmetros:
        n_vertices (int): número de vértices do grafo.
        densidade (float): probabilidade de existir aresta entre dois vértices.

    Retorna:
        nx.Graph: grafo conexo não orientado com pesos nas arestas.
    """
    G = nx.Graph()

    # Adiciona arestas aleatórias com pesos inteiros entre 1 e 100
    for i in range(n_vertices):
        for j in range(i + 1, n_vertices):
            if random.random() < densidade:
                peso = random.randint(1, 100)
                G.add_edge(i, j, weight=peso)

    # Garante que o grafo seja conexo:
    # se tiver mais de um componente, conecta um vértice de cada componente ao próximo.
    if not nx.is_connected(G) and G.number_of_nodes() > 0:
        componentes = list(nx.connected_components(G))
        for i in range(len(componentes) - 1):
            u = list(componentes[i])[0]
            v = list(componentes[i + 1])[0]
            G.add_edge(u, v, weight=random.randint(1, 100))

    return G


if __name__ == "__main__":
    # Tamanhos de grafos para o experimento
    tamanhos = [10, 50, 100]  # se quiser, pode adicionar 200, 500 etc.

    # Vamos acumular os resultados para imprimir em forma de tabela
    linhas = []  # cada linha: (n, algoritmo, tempo, custo)

    for n in tamanhos:
        G = gerar_grafo_teste(n)
        resultados = comparar_agms(G)

        for alg, dados in resultados.items():
            linhas.append((
                n,
                alg,
                dados['tempo'],
                dados['custo'],
            ))

    # Impressão em formato de tabela
    print("\nRESULTADOS DOS EXPERIMENTOS (AGMs)")
    print("-" * 70)
    print(f"{'n':>5} | {'Algoritmo':>15} | {'Tempo (s)':>12} | {'Custo AGM':>12}")
    print("-" * 70)

    for n, alg, tempo, custo in linhas:
        print(f"{n:5d} | {alg:15} | {tempo:12.6f} | {custo:12.2f}")
