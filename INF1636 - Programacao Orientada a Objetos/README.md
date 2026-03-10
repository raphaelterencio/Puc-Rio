# ♟️ Jogo de Xadrez em Java — Programação Orientada a Objetos (2025.1)

Este projeto é um jogo de xadrez desenvolvido em Java como trabalho para a disciplina de **Programação Orientada a Objetos**. O jogo possui interface gráfica, regras oficiais e funcionalidades de salvamento e carregamento de partidas.

---

## 🚀 Como executar

Para rodar o jogo, execute o arquivo:

```bash
Xadrez/src/controller/Main.java
```

---

## 🗂️ Estrutura do Projeto

A estrutura do projeto está organizada da seguinte forma:

```
Xadrez/
├── src/         → Código-fonte principal
│   ├── model/      → Lógica de regras e estado do jogo
│   ├── view/       → Interface gráfica do usuário
│   ├── controller/ → Coordena interação entre view e model
│   └── imagens/    → Recursos gráficos utilizados no jogo
├── tests/       → Testes automatizados com JUnit
```

### 📦 `src/model/`
Contém toda a **lógica do jogo**, como:
- Regras de movimento das peças
- Controle de turno
- Verificações de xeque, xeque-mate, promoção, etc.
- Notificações para a view (padrão **Observer**)

### 🖼️ `src/view/`
Responsável pela **interface gráfica**:
- Renderização do tabuleiro e peças
- Menus e botões
- Exibição de caminhos possíveis e avisos ao jogador

### 🎮 `src/controller/`
Faz a **mediação entre a interface e a lógica**:
- Gerencia eventos de clique
- Envia comandos para o modelo e atualiza a visualização
- Contém um `enum` usado para organizar os tipos de **notificações** enviadas pelo padrão Observer

### 🖼️ `src/imagens/`
Armazena os **recursos visuais** usados na interface do jogo, como ícones e sprites das peças.

---

## 🎮 Funcionalidades

- **Menu Principal**
  - Iniciar novo jogo
  - Carregar jogo salvo

- **Durante o jogo**
  - 🖱️ Clique em uma peça:
    - Caminhos válidos aparecem em **azul**
    - Possibilidade de roque (curto/longo) aparece em **vermelho**
  - Clique com o **botão do meio**: encerra a partida e retorna ao menu principal
  - Clique com o **botão direito**: abre o menu de **salvamento**

- **Alertas visuais e mensagens**:
  - Xeque e xeque-mate
  - Congelamento (jogador não pode mover)
  - Promoção de peão

---

## 🛠️ Padrões de Projeto Utilizados

- **Singleton**  
  Aplicado em `ModelAPI` e `ViewAPI` para garantir instâncias únicas e globais.

- **Facade**  
  Também em `ModelAPI` e `ViewAPI`, simplifica o acesso à lógica do jogo e à interface gráfica.

- **Observer**  
  Implementado em `model/ModelAPI.java`, que envia notificações para `view/Game.java` quando o estado do jogo muda.
  Os tipos de notificação são organizados em um `enum` localizado em `controller/`.

---

## ✅ Testes

Os testes automatizados estão no diretório `Xadrez/tests/` e foram escritos com **JUnit** para validar funcionalidades do modelo de jogo.
