# 🧾 Programação Modular — Sistema de Registro de Vendas (2024.2)

Este repositório contém o trabalho final da disciplina de **Programação Modular**, cursada em 2024.2. O projeto consiste em um **sistema de registro de vendas para um supermercado**, com foco em organização modular e persistência de dados em arquivos.

---

## 🗂️ Estrutura do Repositório
```
├── dados/ → Armazena os arquivos .txt com os dados do sistema
└── src/ → Código-fonte principal do sistema
├── entidades/ → Módulos para entidades principais do sistema
│ ├── cliente.c/.h
│ ├── estoque.c/.h
│ ├── produto.c/.h
│ ├── venda.c/.h
│ └── conversor.c/.h → Converte arquivos entre UTF-8 e UTF-32
├── main.py → Ponto de entrada do sistema
├── menu.py → Interface de terminal para interação com o usuário
├── status_code.py → Define códigos de retorno padronizados
└── testes_unificados.py → Testes automatizados para todas as entidades
```
---

## 💡 Descrição do Projeto

O sistema permite:

- Registrar clientes, produtos e vendas
- Consultar e alterar o estoque
- Armazenar e recuperar dados utilizando **arquivos `.txt`** (em vez de banco de dados, conforme restrição da disciplina)
- Converter arquivos de texto entre **UTF-8 e UTF-32** com o módulo `conversor.c`

---

## 🧪 Testes

Todos os testes foram organizados no arquivo:
src/testes_unificados.py


Esse script realiza testes em conjunto para validar o funcionamento das entidades principais do sistema.

---

## ⚙️ Tecnologias Utilizadas

- **Python** para controle do sistema e terminal
- **C** para implementação de entidades e conversão de arquivos
- Arquivos `.txt` para simulação de banco de dados
