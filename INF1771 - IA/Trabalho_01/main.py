import time
import os
from map_loader import load_map, terrain_cost
from astar import astar, astar_debug
from pairwise import build_pairwise
from tsp_held_karp import solve_tsp_path
from runes import allocate_runes_optimal
from config import TERRAIN_COST, EVENT_DIFFICULTY, RUNES

class EldenRingAgent:
    def __init__(self, map_path="mapa.txt"):
        """Inicializa o agente com o mapa especificado."""
        self.map_path = map_path
        self.map_data = None
        self.pairwise_data = None
        self.travel_order = None
        self.rune_allocation = None
        self.total_cost = 0
        
    def load_world(self):
        """Carrega o mundo e prepara os dados para busca."""
        print("=" * 60)
        print("CARREGANDO MUNDO DE ELDEN RING")
        print("=" * 60)
        
        self.map_data = load_map(self.map_path)
        print(f"[OK] Mapa carregado: {len(self.map_data['grid'])}x{len(self.map_data['grid'][0])}")
        print(f"[INFO] Origem: {self.map_data['start']}")
        print(f"[INFO] Destino: {self.map_data['goal']}")
        print(f"[INFO] Eventos encontrados: {len(self.map_data['events'])}")
        
        # Lista os eventos encontrados
        events = sorted(self.map_data['events'].keys())
        print(f"[INFO] Eventos: {', '.join(events)}")
        
    def calculate_distances(self):
        """Calcula distancias entre todos os pontos de interesse."""
        print("\n" + "=" * 60)
        print("CALCULANDO DISTANCIAS ENTRE PONTOS")
        print("=" * 60)
        
        print("[INFO] Executando A* para todos os pares de pontos...")
        start_time = time.time()
        
        self.pairwise_data = build_pairwise(self.map_data)
        
        elapsed = time.time() - start_time
        print(f"[OK] Distancias calculadas em {elapsed:.2f}s")
        
        labels = self.pairwise_data["labels"]
        dist = self.pairwise_data["dist"]
        
        print(f"[INFO] Total de pontos: {len(labels)}")
        print(f"[INFO] Ordem dos pontos: {' -> '.join(labels)}")
        
        # Mostra algumas distancias importantes
        print(f"\n[INFO] Distancias importantes:")
        print(f"   i -> Z: {dist[0][-1]} minutos")
        for i, label in enumerate(labels[1:-1], 1):  # eventos
            print(f"   i -> {label}: {dist[0][i]} minutos")
            
    def find_optimal_route(self):
        """Encontra a rota otima usando TSP."""
        print("\n" + "=" * 60)
        print("ENCONTRANDO ROTA OTIMA (TSP)")
        print("=" * 60)
        
        labels = self.pairwise_data["labels"]
        dist = self.pairwise_data["dist"]
        
        print("[INFO] Resolvendo TSP com algoritmo Held-Karp...")
        start_time = time.time()
        
        travel_cost, order = solve_tsp_path(labels, dist)
        
        elapsed = time.time() - start_time
        print(f"[OK] TSP resolvido em {elapsed:.2f}s")
        
        if travel_cost == float('inf') or not order:
            print("[ERRO] Nao existe rota viavel!")
            return False
            
        self.travel_order = order
        events_order = [x for x in order if x not in ('i', 'Z')]
        
        print(f"[OK] Rota otima encontrada:")
        print(f"   {' -> '.join(order)}")
        print(f"[INFO] Custo de viagem: {int(travel_cost)} minutos")
        print(f"[INFO] Eventos na ordem: {', '.join(events_order)}")
        
        return True
        
    def allocate_runes(self):
        """Aloca runas de forma otima para os eventos."""
        print("\n" + "=" * 60)
        print("ALOCANDO RUNAS DE FORMA OTIMA")
        print("=" * 60)
        
        events_order = [x for x in self.travel_order if x not in ('i', 'Z')]
        
        print("[INFO] Executando programacao dinamica para alocacao...")
        start_time = time.time()
        
        self.rune_allocation = allocate_runes_optimal(events_order)
        
        elapsed = time.time() - start_time
        print(f"[OK] Alocacao otimizada em {elapsed:.2f}s")
        
        # Mostra detalhes da alocacao
        print(f"\n[INFO] Runas disponiveis:")
        for rune in RUNES:
            print(f"   {rune['name']}: Poder {rune['power']}, Usos {rune['uses']}")
            
        print(f"\n[INFO] Uso final das runas:")
        for name, used in self.rune_allocation["rune_usage"].items():
            print(f"   {name}: {used}/5 usos")
            
        print(f"\n[INFO] Regra 'manter 1 runa inteira': {self.rune_allocation['keep_rule_ok']}")
        
    def show_event_details(self):
        """Mostra detalhes de cada evento."""
        print("\n" + "=" * 60)
        print("DETALHES DOS EVENTOS")
        print("=" * 60)
        
        print(f"{'Evento':<8} {'Dificuldade':<12} {'Runas':<20} {'Poder':<8} {'Tempo':<8}")
        print("-" * 60)
        
        for detail in self.rune_allocation["details"]:
            runes_str = ", ".join(detail["runes"])
            print(f"{detail['label']:<8} {int(detail['D']):<12} {runes_str:<20} {detail['P']:<8.1f} {detail['time']:<8.2f}")
            
    def calculate_final_cost(self):
        """Calcula o custo final total."""
        print("\n" + "=" * 60)
        print("CALCULO DO CUSTO FINAL")
        print("=" * 60)
        
        # Custo de viagem (ja calculado no TSP)
        labels = self.pairwise_data["labels"]
        dist = self.pairwise_data["dist"]
        travel_cost, _ = solve_tsp_path(labels, dist)
        
        # Custo dos eventos
        events_time = self.rune_allocation["total_event_time"]
        
        # Custo total
        self.total_cost = travel_cost + events_time
        
        print(f"[INFO] Custo de viagem (A*): {int(travel_cost)} minutos")
        print(f"[INFO] Tempo dos eventos: {events_time:.2f} minutos")
        print(f"[RESULTADO] CUSTO FINAL: {self.total_cost:.2f} minutos")
        
    def visualize_search(self, show_map=False):
        """Visualiza o processo de busca A*."""
        print("\n" + "=" * 60)
        print("VISUALIZACAO DO PROCESSO DE BUSCA A*")
        print("=" * 60)
        
        # Demonstra A* de i para Z
        print("[INFO] Executando A* de i para Z...")
        dist, path, opened, closed = astar_debug(
            self.map_data, 
            self.map_data['start'], 
            self.map_data['goal'], 
            terrain_cost,
            print_every=1000, 
            print_full=show_map
        )
        
        if dist == float("inf"):
            print("[ERRO] Sem caminho encontrado!")
        else:
            print(f"[OK] Caminho encontrado:")
            print(f"   Custo: {int(dist)} minutos")
            print(f"   Passos: {len(path)-1}")
            print(f"   Estados visitados: {len(closed)}")
            print(f"   Estados na fronteira: {len(opened - closed)}")
            
            if show_map:
                print("\n[INFO] Mapa com visualizacao:")
                from astar import _print_overlay
                _print_overlay(self.map_data['grid'], opened, closed, path, 
                             self.map_data['start'], self.map_data['goal'])
                
    def show_terrain_costs(self):
        """Mostra os custos de cada tipo de terreno."""
        print("\n" + "=" * 60)
        print("CUSTOS DOS TERRENOS")
        print("=" * 60)
        
        terrain_names = {
            'M': 'Montanha (marrom)',
            'A': 'Agua (azul)', 
            'N': 'Neve (verde)',
            'F': 'Floresta (verde)',
            'D': 'Deserto (vermelho)',
            'R': 'Rochoso (cinza)',
            '.': 'Livre (branco)',
            '#': 'Bloqueado'
        }
        
        for terrain, cost in TERRAIN_COST.items():
            name = terrain_names.get(terrain, terrain)
            if cost is None:
                print(f"   {terrain} - {name}: BLOQUEADO")
            else:
                print(f"   {terrain} - {name}: +{cost} minutos")
                
    def show_event_difficulties(self):
        """Mostra as dificuldades dos eventos."""
        print("\n" + "=" * 60)
        print("DIFICULDADES DOS EVENTOS")
        print("=" * 60)
        
        event_names = {
            '1': 'Despertar do Maculado',
            '2': 'Margit, o Agouro Caido', 
            '3': 'Godrick, o Enxertado',
            '4': 'Rennala, Rainha da Lua Cheia',
            '5': 'Contrato de Ranni',
            '6': 'Festival da Guerra de Radahn',
            '7': 'Derrota de Radahn',
            '8': 'Exploracao de Nokron',
            '9': 'Entrada em Altus Plateau',
            '0': 'Morgott, Rei Agouro',
            'B': 'Volcano Manor e Rykard',
            'C': 'Forja dos Gigantes',
            'E': 'Mohg, Senhor do Sangue',
            'G': 'Maliketh, a Lamina Negra',
            'H': 'Godfrey/Hoarah Loux',
            'J': 'Radagon e a Besta Primal'
        }
        
        for event, difficulty in sorted(EVENT_DIFFICULTY.items()):
            name = event_names.get(event, f"Evento {event}")
            print(f"   {event} - {name}: Dificuldade {difficulty}")
            
    def run_complete_simulation(self, show_visualization=True):
        """Executa a simulacao completa do agente."""
        print("ELDEN RING - SIMULACAO COMPLETA DO AGENTE")
        print("=" * 60)
        print("Implementacao: Algoritmo A* + TSP + Alocacao Otima de Runas")
        print("=" * 60)
        
        try:
            # 1. Carregar mundo
            self.load_world()
            
            # 2. Mostrar configuracoes
            self.show_terrain_costs()
            self.show_event_difficulties()
            
            # 3. Calcular distancias
            self.calculate_distances()
            
            # 4. Encontrar rota otima
            if not self.find_optimal_route():
                return False
                
            # 5. Alocar runas
            self.allocate_runes()
            
            # 6. Mostrar detalhes dos eventos
            self.show_event_details()
            
            # 7. Calcular custo final
            self.calculate_final_cost()
            
            # 8. Visualizacao (opcional)
            if show_visualization:
                self.visualize_search(show_map=False)
            
            # 9. Resumo final
            self.show_final_summary()
            
            return True
            
        except Exception as e:
            print(f"[ERRO] Erro durante execucao: {e}")
            return False
            
    def show_final_summary(self):
        """Mostra o resumo final da simulacao."""
        print("\n" + "=" * 60)
        print("RESUMO FINAL DA SIMULACAO")
        print("=" * 60)
        
        print(f"[RESULTADO] Rota otima: {' -> '.join(self.travel_order)}")
        print(f"[RESULTADO] Custo total: {self.total_cost:.2f} minutos")
        print(f"[INFO] Tempo de viagem: {int(self.total_cost - self.rune_allocation['total_event_time'])} minutos")
        print(f"[INFO] Tempo dos eventos: {self.rune_allocation['total_event_time']:.2f} minutos")
        
        print(f"\n[INFO] Alocacao final das runas:")
        for name, used in self.rune_allocation["rune_usage"].items():
            status = "INTACTA" if used == 4 else f"{used}/5 usos"
            print(f"   {name}: {status}")
            
        print(f"\n[INFO] Regra 'manter 1 runa inteira': {self.rune_allocation['keep_rule_ok']}")
        print(f"[OK] Simulacao concluida com sucesso!")

def main():
    """Funcao principal do programa."""
    print("ELDEN RING - AGENTE INTELIGENTE")
    print("INF1771 - Inteligencia Artificial - Trabalho 1")
    print("=" * 60)
    
    # Verifica se o arquivo de mapa existe
    map_path = "mapa.txt"
    if not os.path.exists(map_path):
        print(f"[ERRO] Arquivo de mapa '{map_path}' nao encontrado!")
        print("   Certifique-se de que o arquivo mapa.txt esta no diretorio atual.")
        return
    
    # Cria e executa o agente
    agent = EldenRingAgent(map_path)
    
    # Executa automaticamente a simulacao completa
    print("\n[INFO] Executando simulacao completa...")
    agent.run_complete_simulation(show_visualization=True)

if __name__ == "__main__":
    main()