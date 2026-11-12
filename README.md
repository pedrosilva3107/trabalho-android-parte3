# Notas – Trabalho de Android

Aplicativo de anotações em **Kotlin + Jetpack Compose**. A interface segue os
mockups do grupo: fundo creme, cartões no estilo *post-it* com canto dobrado,
faixa roxa no topo e botão "✓ Salvar".

**Parte 3:** banco de dados SQLite, 2 CRUDs completos, login/cadastro com
controle de sessão e projeto organizado no padrão **MVVM**, na mesma estrutura
de pastas usada nas aulas.

> O banco começa vazio. No primeiro acesso, toque em **"Não tem conta? Cadastre-se"**.

## Estrutura

```
br.edu.up.app
├── MainActivity.kt              → tema + Scaffold (menu inferior) + AppNavigation
├── auth/
│   ├── AuthManager.kt           → hash da senha (PBKDF2 + salt)
│   └── SessaoUsuario.kt         → quem está logado (null = ninguém)
├── data/
│   ├── model/                   → MODEL
│   │   ├── Nota.kt
│   │   └── Usuario.kt
│   ├── local/
│   │   └── AppDatabase.kt       → SQLiteOpenHelper: cria as tabelas
│   └── dal/
│       ├── NotaDao.kt           → CRUD de notas
│       └── UsuarioDao.kt        → CRUD de usuários + senha do login
├── navigation/
│   ├── Routes.kt                → rotas @Serializable
│   ├── AppNavigation.kt         → NavHost + controle de sessão
│   └── Menu.kt                  → menu inferior (Notas, Usuários, Opções, Sair)
├── ui/
│   ├── components/
│   │   └── Componentes.kt       → FaixaTopo, PostitCard, BotaoSalvar, CampoTexto, CampoNota...
│   ├── screens/                 → VIEW
│   │   ├── LoginScreen.kt
│   │   ├── CadastroScreen.kt    → Create de usuário (Criar conta / Novo usuário)
│   │   ├── ListaNotasScreen.kt  → Read de notas
│   │   ├── CriarNotaScreen.kt   → Create de nota
│   │   ├── EditarNotaScreen.kt  → Update / Delete de nota
│   │   ├── UsuariosScreen.kt    → Read de usuários
│   │   ├── EditarUsuarioScreen.kt → Update / Delete de usuário
│   │   └── OpcoesScreen.kt      → aparência
│   ├── theme/
│   │   ├── Color.kt             → cores do app
│   │   ├── Theme.kt             → NotasAppTheme (MaterialTheme claro/escuro)
│   │   ├── Type.kt              → tipografia
│   │   └── Shape.kt             → formato do post-it
│   └── viewmodel/               → VIEWMODEL
│       ├── AuthViewModel.kt     → login e cadastro
│       ├── NotaViewModel.kt     → CRUD de notas
│       ├── UsuarioViewModel.kt  → CRUD de usuários
│       └── OpcoesViewModel.kt   → cor do post-it, fonte, modo escuro
└── validation/
    └── CadastroValidator.kt     → regras dos formulários
```

Fluxo: a **tela** só mostra dados e chama o **ViewModel**. O ViewModel valida
(`CadastroValidator`), roda o acesso ao banco em segundo plano
(`viewModelScope.launch` + `withContext(Dispatchers.IO)`) chamando o **DAO**, e
atualiza seus estados (`mutableStateOf`). A tela se redesenha sozinha.

Cada ViewModel tem uma função `executar { }` que liga o `carregando`, roda a
operação e transforma erros em `mensagem` (`require(...)` vira o texto que
aparece em roxo na tela). Quando uma operação termina, o ViewModel marca
`concluido`/`salvo` e a tela navega dentro de um `LaunchedEffect`.

## Requisitos da Parte 3: onde está cada um

| Requisito | Onde |
|-----------|------|
| `SQLiteOpenHelper` | `data/local/AppDatabase.kt` (`onCreate` cria `usuarios` e `notas`) |
| `writableDatabase` | `cadastrar/inserir`, `alterar` e `excluir` nos DAOs |
| `readableDatabase` | `listar`, `listarPorUsuario`, `buscarPorId` e `buscarSenha` nos DAOs |
| Model | `data/model/Nota.kt`, `data/model/Usuario.kt` |
| **CRUD 1: Notas** | C: `CriarNotaScreen` · R: `ListaNotasScreen` · U e D: `EditarNotaScreen` |
| **CRUD 2: Usuários** | C: `CadastroScreen` · R: `UsuariosScreen` · U e D: `EditarUsuarioScreen` |
| `LaunchedEffect` | carregar listas (`ListaNotasScreen`, `UsuariosScreen`), carregar o item a editar e navegar depois de salvar |
| `LazyColumn` | `ListaNotasScreen` (grade de 2 colunas) e `UsuariosScreen` |
| Login funcional | `AuthViewModel.entrar()` confere o hash da senha (`AuthManager.validarSenha`) |
| Cadastro de usuário | `CadastroScreen` + `AuthViewModel.cadastrar()` |
| Controle de sessão | `auth/SessaoUsuario` + `LaunchedEffect` em `AppNavigation` |
| Menu escondido no login/cadastro | `MainActivity`: o `bottomBar` só aparece com sessão e fora das telas públicas |
| Navegação | `navigation/` com Navigation Compose e rotas `@Serializable` |
| MVVM | `data` (model) / `ui/viewmodel` / `ui/screens` |

### Como funciona a sessão

- `SessaoUsuario.usuario` guarda quem está logado; `null` = ninguém. É um
  `mutableStateOf`, então a navegação e as telas reagem quando muda.
- Login e Cadastro são as únicas telas públicas (`ehPublica()` em `AppNavigation`).
- Um `LaunchedEffect` em `AppNavigation` roda a cada troca de tela: se a tela
  exige login e não há sessão, volta para o Login limpando a pilha.
- Os ViewModels de notas e usuários chamam `SessaoUsuario.exigirUsuario()`
  antes de qualquer acesso ao banco: sem sessão, nada é lido nem gravado.
- **Sair** (menu) chama `SessaoUsuario.encerrar()`.
- Cada usuário vê **só as próprias notas**: toda consulta do `NotaDao` filtra
  por `usuario_id`.
- Ao excluir a própria conta, as notas saem junto (`ON DELETE CASCADE`) e a
  sessão termina.
- Ao editar o próprio nome/usuário, a sessão é atualizada na hora.

## Banco de dados

Arquivo `notas.db` (em `noBackupFilesDir`), versão 1:

```sql
CREATE TABLE usuarios(id INTEGER PRIMARY KEY,
                      nome TEXT NOT NULL, login TEXT NOT NULL UNIQUE,
                      senha BLOB NOT NULL, salt BLOB NOT NULL);
CREATE TABLE notas(id INTEGER PRIMARY KEY,
                   usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                   texto TEXT NOT NULL);
```

- O `AppDatabase` **só cria as tabelas**. Não há dados de exemplo: tudo entra
  pelas telas.
- `onUpgrade` fica vazio porque o banco está na versão 1 (não há versão
  anterior para migrar).
- A senha é guardada como **hash PBKDF2 com salt** (`AuthManager`), nunca em texto.
- As consultas usam `?` com `arrayOf(...)` nos parâmetros (nunca concatenam
  texto digitado no SQL).

## Navegação

Navigation Compose com rotas tipadas (`navigation/Routes.kt`). As rotas são
`@Serializable`; as que precisam de parâmetro são `data class`
(`EditarNota(id)`, `EditarUsuario(id)`, `Login(mensagem)`) e a tela lê o valor
com `toRoute<...>()`.

## Como executar

1. Abra a pasta do projeto no **Android Studio**.
2. Aguarde o Gradle sincronizar.
3. Rode em um emulador ou dispositivo (API 24 ou superior).
4. Toque em "Cadastre-se", crie uma conta e entre.

### Versões

- Android Gradle Plugin 8.13.0 / Gradle 9.1.0 (Kotlin DSL + `libs.versions.toml`)
- Kotlin 2.2.20
- compileSdk 36 / minSdk 24
- Jetpack Compose BOM 2024.12.01 (Material 3)
- navigation-compose 2.8.9 + kotlinx-serialization-json 1.7.3
