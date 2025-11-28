

# Dimensão do mapa
MAP_ROWS = 137
MAP_COLS = 320

# Custos por terreno 
TERRAIN_COST = {
    'M': 50,  # Montanha
    'A': 20,  # Água
    'N': 15,  # Neve
    'L': 15,  # Lava
    'F': 10,  # Floresta
    'D': 8,   # Deserto
    'R': 5,   # Rochoso
    '.': 1,   # Livre
    'i': 1,   # Origem 
    'Z': 1,   # Destino 
    '#': None # Bloqueado
}


EVENT_CHARS = ['1','2','3','4','5','6','7','8','9','0','B','C','E','G','H','J']

# Dificuldades por evento
EVENT_DIFFICULTY = {
    '1': 55,
    '2': 60,
    '3': 65,
    '4': 70,
    '5': 75,
    '6': 90,
    '7': 95,
    '8': 120,
    '9': 125,
    '0': 130,
    'B': 135,
    'C': 150,
    'E': 155,
    'G': 160,
    'H': 170,
    'J': 180,
}

# Runas e limites
RUNES = [
    {"name": "Godrick", "power": 1.6, "uses": 5},
    {"name": "Radahn",  "power": 1.4, "uses": 5},
    {"name": "Morgott", "power": 1.3, "uses": 5},
    {"name": "Malenia", "power": 1.2, "uses": 5},
    {"name": "Rykard",  "power": 1.0, "uses": 5},
]

KEEP_ONE_RUNE_INTACT = True
