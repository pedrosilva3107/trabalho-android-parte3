# Notas – Trabalho de Android

Aplicativo de anotações em **Kotlin + Jetpack Compose**. A interface segue os
mockups do grupo: fundo creme, cartões no estilo *post-it* com canto dobrado,
faixa roxa no topo e botão "✓ Salvar".

O app tem **4 CRUDs** (Notas, Categorias, Locais e Usuários), login/cadastro com
controle de sessão, banco SQLite, consulta de CEP pela API **ViaCEP com Retrofit**
e o recurso nativo de **mapas** (abre o endereço do local no app de mapas).
O projeto segue o padrão **MVVM**, na mesma estrutura de pastas usada nas aulas.

> O banco começa vazio. No primeiro acesso, toque em **"Não tem conta? Cadastre-se"**.

## Estrutura

```
br.edu.up.app
├── MainActivity.kt              → tema + Menu
├── auth/
│   ├── AuthManager.kt           → hash da senha (PBKDF2 + salt)
│   └── SessaoUsuario.kt         → quem está logado (null = ninguém)
├── data/
│   ├── Endereco.kt              → resposta da API ViaCEP
│   ├── ViaCepService.kt         → Retrofit (GET {cep}/json/)
│   ├── model/                   → MODEL
│   │   ├── Nota.kt
│   │   ├── Categoria.kt
│   │   ├── Local.kt
│   │   └── Usuario.kt
│   ├── local/
│   │   └── AppDatabase.kt       → SQLiteOpenHelper: cria as tabelas
│   └── dal/
│       ├── NotaDao.kt           → CRUD de notas
│       ├── CategoriaDao.kt      → CRUD de categorias
│       ├── LocalDao.kt          → CRUD de locais
│       └── UsuarioDao.kt        → CRUD de usuários + senha do login
├── navigation/
│   ├── Routes.kt                → rotas @Serializable
│   ├── AppNavigation.kt         → NavHost + controle de sessão
│   └── Menu.kt                  → Scaffold + TopAppBar (☰) + menu lateral (ModalNavigationDrawer)
├── ui/
│   ├── components/
│   │   └── Componentes.kt       → FaixaTopo, PostitCard, BotaoSalvar, CampoTexto, SeletorCategoria...
│   ├── screens/                 → VIEW
│   │   ├── LoginScreen.kt
│   │   ├── CadastroScreen.kt    → Create de usuário (Criar conta / Novo usuário)
│   │   ├── ListaNotasScreen.kt  → Read de notas
│   │   ├── CriarNotaScreen.kt   → Create de nota
│   │   ├── EditarNotaScreen.kt  → Update / Delete de nota
│   │   ├── CategoriasScreen.kt  → Read de categorias
│   │   ├── CategoriaFormScreen.kt → Create / Update / Delete de categoria
│   │   ├── LocaisScreen.kt      → Read de locais
│   │   ├── LocalFormScreen.kt   → Create / Update / Delete de local + CEP + mapa
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
│       ├── CategoriaViewModel.kt → CRUD de categorias
│       ├── LocalViewModel.kt    → CRUD de locais + consulta do CEP (Retrofit)
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

## Requisitos do trabalho: onde está cada um

| Requisito | Onde |
|-----------|------|
| MVVM | `data` (model + DAO) / `ui/viewmodel` / `ui/screens` |
| Jetpack Compose, mínimo 6 telas | 12 telas em `ui/screens` |
| `Scaffold` | `navigation/Menu.kt` (barra do topo + conteúdo) |
| `LaunchedEffect` | carregar listas, carregar o item a editar, navegar depois de salvar e o controle de sessão em `AppNavigation` |
| `LazyColumn` | `ListaNotasScreen`, `CategoriasScreen`, `LocaisScreen` e `UsuariosScreen` |
| `NavHost` e rotas serializadas | `navigation/AppNavigation.kt` e `navigation/Routes.kt` (`@Serializable`) |
| Login | `LoginScreen` + `AuthViewModel.entrar()` (confere o hash da senha) |
| Cadastro de usuários | `CadastroScreen` + `AuthViewModel.cadastrar()` |
| Controle de sessão | `auth/SessaoUsuario` + `LaunchedEffect` em `AppNavigation` |
| Menu escondido no login e cadastro | `Menu.kt`: sem barra do topo e com o menu lateral travado (`gesturesEnabled`) nas telas públicas |
| **CRUD 1: Notas** | C: `CriarNotaScreen` · R: `ListaNotasScreen` · U e D: `EditarNotaScreen` |
| **CRUD 2: Categorias** | C, U e D: `CategoriaFormScreen` · R: `CategoriasScreen` |
| **CRUD 3: Locais** | C, U e D: `LocalFormScreen` · R: `LocaisScreen` |
| **CRUD 4: Usuários** | C: `CadastroScreen` · R: `UsuariosScreen` · U e D: `EditarUsuarioScreen` |
| `SQLiteOpenHelper` | `data/local/AppDatabase.kt` |
| `writableDatabase` | `inserir/cadastrar`, `alterar` e `excluir` nos DAOs |
| `readableDatabase` | `listar`, `listarPorUsuario`, `buscarPorId` e `buscarSenha` nos DAOs |
| Model | `data/model/` (`Nota`, `Categoria`, `Local`, `Usuario`) |
| Retrofit (API) | `data/ViaCepService.kt` + `LocalViewModel.buscarCep()`: preenche o endereço pelo CEP |
| Recurso nativo (mapas) | `LocalFormScreen`: botão **Abrir no mapa** (`Intent.ACTION_VIEW` com `geo:`) |

### Como funciona a sessão

- `SessaoUsuario.usuario` guarda quem está logado; `null` = ninguém. É um
  `mutableStateOf`, então a navegação e as telas reagem quando muda.
- Login e Cadastro são as únicas telas públicas (`hasRoute<Login>()` / `hasRoute<Cadastro>()`).
- Um `LaunchedEffect` em `AppNavigation` roda a cada troca de tela: se a tela
  exige login e não há sessão, volta para o Login limpando a pilha.
- Os ViewModels chamam `SessaoUsuario.exigirUsuario()` antes de qualquer acesso
  ao banco: sem sessão, nada é lido nem gravado.
- **Sair** (menu) chama `SessaoUsuario.encerrar()`.
- Cada usuário vê **só os próprios dados**: toda consulta de notas, categorias e
  locais filtra por `usuario_id`.
- Ao excluir um usuário, as notas, categorias e locais dele saem junto
  (o `UsuarioDao.excluir` apaga tudo antes do usuário). Se for a própria conta,
  a sessão termina.
- Ao editar o próprio nome/usuário, a sessão é atualizada na hora.

## Banco de dados

Arquivo `notas.db` (em `noBackupFilesDir`), versão 2:

```sql
CREATE TABLE usuarios(id INTEGER PRIMARY KEY,
                      nome TEXT NOT NULL, login TEXT NOT NULL UNIQUE,
                      senha BLOB NOT NULL, salt BLOB NOT NULL);
CREATE TABLE notas(id INTEGER PRIMARY KEY,
                   usuario_id INTEGER NOT NULL,
                   texto TEXT NOT NULL,
                   categoria_id INTEGER NOT NULL DEFAULT 0);   -- 0 = sem categoria
CREATE TABLE categorias(id INTEGER PRIMARY KEY,
                        usuario_id INTEGER NOT NULL,
                        nome TEXT NOT NULL);
CREATE TABLE locais(id INTEGER PRIMARY KEY,
                    usuario_id INTEGER NOT NULL,
                    nome TEXT NOT NULL, cep TEXT NOT NULL, endereco TEXT NOT NULL);
```

- O `AppDatabase` **só cria as tabelas**. Não há dados de exemplo: tudo entra
  pelas telas.
- `onUpgrade`: quem tinha a versão 1 (só usuários e notas) ganha as tabelas
  novas e a coluna `categoria_id` sem perder o que já estava salvo.
- Ao excluir uma categoria, as notas dela não são apagadas: ficam "sem categoria".
- A senha é guardada como **hash PBKDF2 com salt** (`AuthManager`), nunca em texto.
- As consultas usam `?` com `arrayOf(...)` nos parâmetros (nunca concatenam
  texto digitado no SQL).

## API (Retrofit) e mapa

- Na tela de local, **Buscar endereço pelo CEP** chama
  `https://viacep.com.br/ws/{cep}/json/` com Retrofit + Gson e preenche o endereço.
  CEP inválido, CEP inexistente e falta de internet viram mensagem na tela.
- **Abrir no mapa** abre o endereço salvo no app de mapas do celular.
- Permissão no `AndroidManifest.xml`: `INTERNET`.

## Navegação

Navigation Compose com rotas tipadas (`navigation/Routes.kt`). As rotas são
`@Serializable`; as que precisam de parâmetro são `data class`
(`EditarNota(id)`, `EditarUsuario(id)`, `CategoriaForm(id)`, `LocalForm(id)`,
`Login(mensagem)`) e a tela lê o valor com `toRoute<...>()`.
Em `CategoriaForm` e `LocalForm`, `id = 0` abre o formulário para criar.

## Como executar

1. Abra a pasta do projeto no **Android Studio**.
2. Aguarde o Gradle sincronizar.
3. Rode em um emulador ou dispositivo (API 24 ou superior) com internet.
4. Toque em "Cadastre-se", crie uma conta e entre.

### Versões

- Android Gradle Plugin 8.13.0 / Gradle 9.1.0 (Kotlin DSL + `libs.versions.toml`)
- Kotlin 2.2.20
- compileSdk 36 / minSdk 24
- Jetpack Compose BOM 2024.12.01 (Material 3)
- navigation-compose 2.8.9 + kotlinx-serialization-json 1.7.3
- Retrofit 2.11.0 + converter-gson 2.11.0
