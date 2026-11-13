package br.edu.up.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.ui.components.FaixaTopo
import br.edu.up.app.ui.viewmodel.OpcoesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(opcoesViewModel: OpcoesViewModel) {
    val navController = rememberNavController()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination

    val itens = listOf(
        Triple(
            "Notas", ListaNotas,
            (destino?.hasRoute<ListaNotas>() ?: false) ||
                    (destino?.hasRoute<CriarNota>() ?: false) ||
                    (destino?.hasRoute<EditarNota>() ?: false)
        ),
        Triple(
            "Categorias", Categorias,
            (destino?.hasRoute<Categorias>() ?: false) ||
                    (destino?.hasRoute<CategoriaForm>() ?: false)
        ),
        Triple(
            "Locais", Locais,
            (destino?.hasRoute<Locais>() ?: false) ||
                    (destino?.hasRoute<LocalForm>() ?: false)
        ),
        Triple(
            "Usuários", Usuarios,
            (destino?.hasRoute<Usuarios>() ?: false) ||
                    (destino?.hasRoute<NovoUsuario>() ?: false) ||
                    (destino?.hasRoute<EditarUsuario>() ?: false)
        ),
        Triple("Opções", Opcoes, destino?.hasRoute<Opcoes>() ?: false)
    )
    val titulo = itens.firstOrNull { it.third }?.first ?: "Notas"

    val telaPublica = (destino?.hasRoute<Login>() ?: false) ||
            (destino?.hasRoute<Cadastro>() ?: false)
    val mostrarMenu = SessaoUsuario.usuario != null && destino != null && !telaPublica

    val menu = rememberDrawerState(DrawerValue.Closed)
    val escopo = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = menu,
        gesturesEnabled = mostrarMenu,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Menu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    itens.forEach { (nome, rota, selecionado) ->
                        NavigationDrawerItem(
                            label = { Text(nome, color = MaterialTheme.colorScheme.onBackground) },
                            selected = selecionado,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = {
                                escopo.launch {
                                    menu.close()
                                    if (!selecionado) {
                                        navController.navigate(rota) {
                                            popUpTo<ListaNotas>()
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                    NavigationDrawerItem(
                        label = { Text("Sair", color = MaterialTheme.colorScheme.onBackground) },
                        selected = false,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            escopo.launch {
                                menu.close()
                                SessaoUsuario.encerrar()
                                navController.irParaLogin()
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (mostrarMenu) {
                    Column {
                        TopAppBar(
                            title = { Text(titulo) },
                            navigationIcon = {
                                BotaoMenu { escopo.launch { menu.open() } }
                            }
                        )
                        FaixaTopo()
                    }
                } else {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        FaixaTopo()
                    }
                }
            }
        ) { innerPadding ->
            AppNavigation(
                navController = navController,
                opcoesViewModel = opcoesViewModel,
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}

@Composable
private fun BotaoMenu(abrirMenu: () -> Unit) {
    IconButton(onClick = abrirMenu) {
        Text(
            text = "☰",
            fontSize = 24.sp
        )
    }
}
