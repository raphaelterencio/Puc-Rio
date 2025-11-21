from collections import deque
import networkx as nx

def eh_bipartido(G: nx.Graph) -> tuple[bool, dict]:

    """
    Testa se um grafo é bipartido.
    
    Parâmetros:
        G (nx.Graph): Grafo não orientado
    
    Retorna:
        tuple[bool, dict]: (True/False, dicionário de partições)
                           Se bipartido, o dicionário mapeia vértices para {0, 1}
                           indicando a qual partição pertencem.
                           Se não bipartido, retorna dicionário vazio.
    """

    cores = {}  # dicionário: vértice -> 0 ou 1

    
    for v_inicial in G.nodes():
        if v_inicial in cores:
            
            continue

        
        cores[v_inicial] = 0
        fila = deque([v_inicial])

        while fila:
            u = fila.popleft()

            
            for w in G.neighbors(u):
                if w not in cores:
                    
                    cores[w] = 1 - cores[u]
                    fila.append(w)
                else:
                    
                    if cores[w] == cores[u]:
                        return False, {}

    
    return True, cores


if __name__ == "__main__":
    # Teste 1: grafo bipartido (ciclo de 4 vértices)
    G1 = nx.Graph()
    G1.add_edges_from([
        (1, 2),
        (2, 3),
        (3, 4),
        (4, 1),
    ])
    eh1, cores1 = eh_bipartido(G1)
    print("G1 é bipartido?", eh1)
    print("cores G1:", cores1)
    if eh1:
        lado0 = [v for v, c in cores1.items() if c == 0]
        lado1 = [v for v, c in cores1.items() if c == 1]
        print("Partição G1:", lado0, "|", lado1)

    print()

    # Teste 2: grafo NÃO bipartido (triângulo)
    G2 = nx.Graph()
    G2.add_edges_from([
        (1, 2),
        (2, 3),
        (3, 1),
    ])
    eh2, cores2 = eh_bipartido(G2)
    print("G2 é bipartido?", eh2)
    print("cores G2:", cores2)
