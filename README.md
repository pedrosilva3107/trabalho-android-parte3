# Notas – Trabalho de Android

Aplicativo de anotações em **Kotlin + Jetpack Compose**, usando apenas os
conceitos vistos em aula (Aulas 01 a 09). A interface segue os mockups do grupo:
fundo creme, cartões no estilo *post-it* com canto dobrado, faixa roxa no topo e
botão "✓ Salvar".

## Telas

A tela de **Login** não conta como uma das telas do trabalho. As 4 telas
principais (uma por integrante) são:

| # | Arquivo | Tela | Responsável |
|---|---------|------|-------------|
| – | `TelaLogin.kt` | Login (usuário + senha) | (comum) |
| 1 | `TelaListaNotas.kt` | Grade de notas (`LazyColumn` em 2 colunas) + botões flutuantes | Integrante 1 |
| 2 | `TelaCriarNota.kt` | Formulário para criar nota | Integrante 2 |
| 3 | `TelaEditarNota.kt` | Editar o texto de uma nota / excluir | Integrante 3 |
| 4 | `TelaOpcoes.kt` | Cor do post-it, tamanho da fonte e modo escuro | Integrante 4 |

> Preencham a coluna "Responsável" com os nomes reais do grupo.

## Diferenças em relação ao mockup (e por quê)

- **Slider e Switch da tela Opções** viraram **botões** (− / + para a fonte, e um
  botão "Ligado/Desligado" para o modo escuro). Slider e Switch não foram usados
  em aula.
- **Botão "Excluir nota"** foi adicionado na tela de edição. O mockup não tem
  nenhum lugar para apagar uma nota; a tela de edição é o lugar mais natural.
- **Botão "‹ Voltar"** no topo de Criar/Editar/Opções. O mockup não mostra, mas o
  app precisa de um jeito de voltar (não usamos biblioteca de navegação).
- O ícone de engrenagem é desenhado com o caractere "⚙".

## Como a navegação funciona

Sem biblioteca de navegação (não foi vista em aula). A troca de telas é feita só
com **estado** em `AppNotas.kt`:

- um `enum class Tela` com as telas possíveis;
- uma variável `telaAtual` (`remember { mutableStateOf(...) }`);
- um `when (telaAtual)` que decide qual `@Composable` mostrar;
- cada tela recebe funções de callback (`onSalvar`, `onVoltar`, ...).

As notas ficam numa lista observável em memória (`mutableStateListOf`). As opções
(cor, fonte, modo escuro) também são estado em `AppNotas.kt` e são passadas para
todas as telas, por isso mudam o app inteiro na hora.

## Conceitos de aula utilizados

- **Aulas 01–03** – tipos, `if/when`, `for` (com `step`), funções e lambdas (callbacks).
- **Aula 04** – listas (`listOf`, `mutableStateListOf`), `enum class`, `data class`.
- **Aula 05–06** – Compose, `Row`, `Column`, `Box`.
- **Aulas 07–09** – formulários (`OutlinedTextField`, `TextField`, `Button`, validação) e `LazyColumn`.

## Como executar

1. Abra a pasta do projeto no **Android Studio**.
2. Aguarde o Gradle sincronizar (baixa SDK/dependências se faltar).
3. Rode em um emulador ou dispositivo (API 24 ou superior).

### Versões

- Android Gradle Plugin 8.13.0 / Gradle 9.1.0
- Kotlin 2.2.20
- compileSdk 36 / minSdk 24
- Jetpack Compose BOM 2024.12.01 (Material 3)

Se o Android Studio for mais antigo, ele oferece para ajustar essas versões na
primeira sincronização.
