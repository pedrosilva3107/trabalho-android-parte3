package br.edu.up.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.ui.components.FaixaTopo

// Menu inferior. Só aparece com usuário logado (nunca no login/cadastro).
@Composable
fun Menu(navController: NavHostController, destino: NavDestination?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        FaixaTopo()
        Row(modifier = Modifier.fillMaxWidth()) {
            ItemMenu(
                texto = "Notas",
                selecionado = destino != null && (destino.hasRoute<ListaNotas>() ||
                        destino.hasRoute<CriarNota>() || destino.hasRoute<EditarNota>()),
                modifier = Modifier.weight(1f),
                onClick = { navController.irParaAba(ListaNotas) }
            )
            ItemMenu(
                texto = "Usuários",
                selecionado = destino != null && (destino.hasRoute<Usuarios>() ||
                        destino.hasRoute<NovoUsuario>() || destino.hasRoute<EditarUsuario>()),
                modifier = Modifier.weight(1f),
                onClick = { navController.irParaAba(Usuarios) }
            )
            ItemMenu(
                texto = "Opções",
                selecionado = destino?.hasRoute<Opcoes>() == true,
                modifier = Modifier.weight(1f),
                onClick = { navController.irParaAba(Opcoes) }
            )
            ItemMenu(
                texto = "Sair",
                selecionado = false,
                modifier = Modifier.weight(1f),
                onClick = {
                    SessaoUsuario.encerrar()
                    navController.irParaLogin()
                }
            )
        }
    }
}

// Troca de aba sem empilhar telas: a lista de notas fica sempre na base da pilha.
private fun NavHostController.irParaAba(rota: Any) {
    navigate(rota) {
        popUpTo(ListaNotas)
        launchSingleTop = true
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
