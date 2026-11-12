package br.edu.up.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.ui.components.FaixaTopo

// Altura total do menu (faixa roxa de 4dp + 48dp dos botões).
// A MainActivity deixa esse espaço livre embaixo das telas.
val ALTURA_MENU = 52.dp

// Menu inferior. Só aparece com usuário logado (nunca no login/cadastro).
@Composable
fun Menu(navController: NavHostController) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination

    // (nome, rota, selecionado)
    val itens = listOf(
        Triple(
            "Notas", ListaNotas,
            (destino?.hasRoute<ListaNotas>() ?: false) ||
                    (destino?.hasRoute<CriarNota>() ?: false) ||
                    (destino?.hasRoute<EditarNota>() ?: false)
        ),
        Triple(
            "Usuários", Usuarios,
            (destino?.hasRoute<Usuarios>() ?: false) ||
                    (destino?.hasRoute<NovoUsuario>() ?: false) ||
                    (destino?.hasRoute<EditarUsuario>() ?: false)
        ),
        Triple("Opções", Opcoes, destino?.hasRoute<Opcoes>() ?: false)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(ALTURA_MENU),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            FaixaTopo()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 4 abas do mesmo tamanho: cada uma ocupa uma parte do espaço que sobra
                // (1/4 da linha, 1/3 do resto, 1/2 do resto e o resto todo).
                itens.forEachIndexed { i, (nome, rota, selecionado) ->
                    ItemMenu(
                        texto = nome,
                        selecionado = selecionado,
                        modifier = Modifier.fillMaxWidth(1f / (4 - i)),
                        onClick = {
                            // Troca de aba sem empilhar telas: a lista de notas fica sempre na base da pilha.
                            navController.navigate(rota) {
                                popUpTo<ListaNotas>()
                                launchSingleTop = true
                            }
                        }
                    )
                }
                ItemMenu(
                    texto = "Sair",
                    selecionado = false,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        SessaoUsuario.encerrar()
                        navController.irParaLogin()
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemMenu(
    texto: String,
    selecionado: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = texto,
            color = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}
