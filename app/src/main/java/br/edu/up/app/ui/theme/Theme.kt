package br.edu.up.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Onde cada cor é usada nas telas:
//   background       → fundo creme das telas
//   onBackground     → títulos e textos
//   onSurfaceVariant → textos secundários ("4 notas", "Olá, ...")
//   primary          → roxo (faixa do topo, erros, item do menu selecionado)
//   surface          → botão "✓ Salvar" e menu inferior
private val LightColorScheme = lightColorScheme(
    primary = Roxo,
    background = Creme,
    onBackground = TextoEscuro,
    surface = BotaoClaro,
    onSurface = TextoEscuro,
    onSurfaceVariant = TextoFracoClaro
)

private val DarkColorScheme = darkColorScheme(
    primary = RoxoClaro,
    background = FundoEscuro,
    onBackground = TextoClaro,
    surface = BotaoEscuro,
    onSurface = TextoClaro,
    onSurfaceVariant = TextoFracoEscuro
)

// O modo escuro vem da tela de Opções (não do sistema), por isso começa desligado.
@Composable
fun NotasAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
