# Notas – Trabalho de Android

Aplicativo de anotações em **Kotlin + Jetpack Compose**. A interface segue os
mockups do grupo: fundo creme, cartões no estilo *post-it* com canto dobrado,
faixa roxa no topo e botão "✓ Salvar".

**Parte 3:** banco de dados SQLite, 2 CRUDs completos, login/cadastro com
controle de sessão e projeto organizado no padrão **MVVM**.

> Usuário de demonstração criado na primeira execução: **admin / 1234**
> (já vem com 4 notas).

## Estrutura MVVM

```
br.edu.up.app
├── MainActivity.kt          → só chama AppNotas()
├── model/                   → MODEL (dados)
│   ├── Nota.kt              → data class da tabela "notas"
│   └── Usuario.kt           → data class da tabela "usuarios"
├── data/                    → acesso ao banco (parte do Model)
│   ├── BancoHelper.kt       → SQLiteOpenHelper: cria as tabelas (onCreate/onUpgrade)
│   ├── NotaRepository.kt    → CRUD de notas (writableDatabase / readableDatabase)
│   └── UsuarioRepository.kt → CRUD de usuários + busca por login
├── viewmodel/               → VIEWMODEL (regras e estado das telas)
│   ├── NotaViewModel.kt     → lista de notas, validação, criar/editar/excluir
│   ├── UsuarioViewModel.kt  → login, sessão, cadastro, editar/excluir usuário
│   └── OpcoesViewModel.kt   → cor do post-it, fonte, modo escuro
└── view/                    → VIEW (telas Compose)
    ├── AppNotas.kt          → navegação por estado + controle de sessão + menu
    ├── Componentes.kt       → FaixaTopo, PostitCard, BotaoSalvar, CampoTexto, MenuInferior
    ├── Tema.kt              → cores e formato do post-it
    ├── TelaLogin.kt         → login
    ├── TelaCadastro.kt      → cadastro de usuário (Create)
    ├── TelaListaNotas.kt    → lista de notas (Read)
    ├── TelaCriarNota.kt     → nova nota (Create)
    ├── TelaEditarNota.kt    → editar nota (Update) / excluir (Delete)
    ├── TelaUsuarios.kt      → lista de usuários (Read)
    ├── TelaEditarUsuario.kt → editar usuário (Update) / excluir (Delete)
    └── TelaOpcoes.kt        → opções de aparência
```

Fluxo: a **View** só mostra dados e repassa os toques ao **ViewModel**. O
ViewModel valida, chama o **Repository**, que usa o `BancoHelper` para gravar ou
ler do SQLite, e depois recarrega a lista. Como as listas do ViewModel são
`mutableStateListOf` e os campos são `mutableStateOf`, a tela se redesenha
sozinha.

Os ViewModels de notas e usuários estendem `AndroidViewModel` porque o
`SQLiteOpenHelper` precisa de um `Context` (a `Application`). Eles são criados
com `viewModel()` em `AppNotas.kt`.

## Requisitos da Parte 3: onde está cada um

| Requisito | Onde |
|-----------|------|
| `SQLiteOpenHelper` | `data/BancoHelper.kt` (`onCreate` cria `usuarios` e `notas`) |
| `writableDatabase` | `inserir`, `atualizar` e `deletar` nos dois repositories |
| `readableDatabase` | `listarPorUsuario` (notas), `listar` e `buscarPorLogin` (usuários) |
| Model | `model/Nota.kt`, `model/Usuario.kt` |
| **CRUD 1: Notas** | C: `TelaCriarNota` · R: `TelaListaNotas` · U e D: `TelaEditarNota` |
| **CRUD 2: Usuários** | C: `TelaCadastro` · R: `TelaUsuarios` · U e D: `TelaEditarUsuario` |
| `LaunchedEffect` | `TelaListaNotas` (carrega as notas do usuário) e `TelaUsuarios` (carrega os usuários) |
| `LazyColumn` | `TelaListaNotas` (grade de 2 colunas) e `TelaUsuarios` |
| Login funcional | `UsuarioViewModel.entrar()` confere usuário e senha no banco |
| Cadastro de usuário | `TelaCadastro` + `UsuarioViewModel.cadastrar()` (valida campos, senha mínima, confirmação e usuário repetido) |
| Controle de sessão | `UsuarioViewModel.usuarioLogado`; botão **Sair** do menu chama `sair()` |
| Menu escondido no login/cadastro | `AppNotas.kt`: se `usuarioLogado == null` só existem Login e Cadastro, e o `MenuInferior` não é desenhado |
| MVVM | pastas `model` / `data` / `viewmodel` / `view` |

### Como funciona a sessão

- `usuarioLogado` (no `UsuarioViewModel`) guarda quem está logado; `null` significa
  "ninguém logado".
- `AppNotas.kt` decide o que mostrar com um `if`: sem sessão, só Login/Cadastro;
  com sessão, a tela escolhida mais o menu inferior (Notas, Usuários, Opções, Sair).
- Cada usuário vê **só as próprias notas** (coluna `usuario_id` na tabela `notas`).
- Ao excluir a própria conta, as notas dele são apagadas e a sessão termina.
- Ao editar o próprio nome/usuário, a sessão é atualizada na hora.

## Banco de dados

Arquivo `notas.db`, versão 1:

```sql
CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT,
                       nome TEXT NOT NULL, login TEXT NOT NULL UNIQUE, senha TEXT NOT NULL);
CREATE TABLE notas    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                       usuario_id INTEGER NOT NULL, texto TEXT NOT NULL);
```

As consultas usam `?` com `arrayOf(...)` nos parâmetros (nunca concatenam texto
digitado no SQL).

## Navegação

Sem biblioteca de navegação. A troca de telas é feita com **estado** em
`AppNotas.kt`: um `enum class Tela`, a variável `telaAtual`
(`remember { mutableStateOf(...) }`) e um `when (telaAtual)`. Cada tela recebe o
ViewModel de que precisa e funções de callback (`onVoltar`, `onCriar`, ...).

## Diferenças em relação ao mockup (e por quê)

- **Slider e Switch da tela Opções** viraram **botões** (− / + para a fonte, e um
  botão "Ligado/Desligado" para o modo escuro).
- **Botões "Excluir"** nas telas de edição de nota e de usuário. A exclusão de
  usuário pede um segundo toque para confirmar.
- **Menu inferior** (Notas, Usuários, Opções, Sair) no lugar do botão de
  engrenagem, porque a Parte 3 pede menu e o app ganhou a área de usuários.
- **Botão "‹ Voltar"** no topo das telas de criar/editar.

## Limitações conhecidas

- A senha é guardada em texto puro no banco (criptografia/hash não foi vista em aula).
- A sessão fica na memória do ViewModel: ao fechar o app por completo, é preciso
  entrar de novo. As opções de aparência também voltam ao padrão.

## Como executar

1. Abra a pasta do projeto no **Android Studio**.
2. Aguarde o Gradle sincronizar.
3. Rode em um emulador ou dispositivo (API 24 ou superior).
4. Entre com **admin / 1234** ou crie uma conta em "Cadastre-se".

### Versões

- Android Gradle Plugin 8.13.0 / Gradle 9.1.0
- Kotlin 2.2.20
- compileSdk 36 / minSdk 24
- Jetpack Compose BOM 2024.12.01 (Material 3)
- lifecycle-viewmodel-compose 2.8.7 (para `viewModel()`)
