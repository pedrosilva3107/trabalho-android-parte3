package br.edu.up.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
