package br.edu.up.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.ui.screens.CadastroScreen
import br.edu.up.app.ui.screens.CriarNotaScreen
import br.edu.up.app.ui.screens.EditarNotaScreen
import br.edu.up.app.ui.screens.EditarUsuarioScreen
import br.edu.up.app.ui.screens.ListaNotasScreen
import br.edu.up.app.ui.screens.LoginScreen
import br.edu.up.app.ui.screens.OpcoesScreen
import br.edu.up.app.ui.screens.UsuariosScreen
import br.edu.up.app.ui.theme.CORES_POSTIT
import br.edu.up.app.ui.viewmodel.OpcoesViewModel

// Login e Cadastro são as únicas telas que abrem sem sessão.
fun NavDestination.ehPublica(): Boolean = hasRoute<Login>() || hasRoute<Cadastro>()

// Sai de qualquer tela e volta ao login, limpando a pilha (o "voltar" não retorna ao app).
fun NavHostController.irParaLogin(mensagem: String = "") {
    navigate(Login(mensagem)) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    opcoesViewModel: OpcoesViewModel,
    modifier: Modifier = Modifier
) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val usuarioLogado = SessaoUsuario.usuario

    val corPostit = CORES_POSTIT[opcoesViewModel.corPostitIndex]
    val tamanhoFonte = opcoesViewModel.tamanhoFonte

    // Controle de sessão: se a tela atual exige login e ninguém está logado,
    // manda para o Login. Roda de novo a cada troca de tela ou de sessão.
    LaunchedEffect(entradaAtual, usuarioLogado) {
        val destino = navController.currentDestination ?: return@LaunchedEffect
        if (!destino.ehPublica() && SessaoUsuario.usuario == null) {
            navController.irParaLogin()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login(),
        modifier = modifier
    ) {

        composable<Login> { entrada ->
            val rota = entrada.toRoute<Login>()
            LoginScreen(
                authViewModel = viewModel(),
                corPostit = corPostit,
                mensagem = rota.mensagem,
                onEntrou = {
                    navController.navigate(ListaNotas) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onCadastrar = { navController.navigate(Cadastro) }
            )
        }

        composable<Cadastro> {
            CadastroScreen(
                authViewModel = viewModel(),
                corPostit = corPostit,
                titulo = "Criar conta",
                onConcluido = { navController.irParaLogin("Conta criada. Faça login.") },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<ListaNotas> {
            val usuario = SessaoUsuario.usuario ?: return@composable
            ListaNotasScreen(
                notaViewModel = viewModel(),
                usuario = usuario,
                corPostit = corPostit,
                tamanhoFonte = tamanhoFonte,
                onCriar = { navController.navigate(CriarNota) },
                onAbrir = { id -> navController.navigate(EditarNota(id)) }
            )
        }

        composable<CriarNota> {
            CriarNotaScreen(
                notaViewModel = viewModel(),
                corPostit = corPostit,
                tamanhoFonte = tamanhoFonte,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<EditarNota> { entrada ->
            val rota = entrada.toRoute<EditarNota>()
            EditarNotaScreen(
                notaId = rota.id,
                notaViewModel = viewModel(),
                corPostit = corPostit,
                tamanhoFonte = tamanhoFonte,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<Usuarios> {
            UsuariosScreen(
                usuarioViewModel = viewModel(),
                corPostit = corPostit,
                onNovo = { navController.navigate(NovoUsuario) },
                onAbrir = { id -> navController.navigate(EditarUsuario(id)) }
            )
        }

        composable<NovoUsuario> {
            CadastroScreen(
                authViewModel = viewModel(),
                corPostit = corPostit,
                titulo = "Novo usuário",
                onConcluido = { navController.popBackStack() },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<EditarUsuario> { entrada ->
            val rota = entrada.toRoute<EditarUsuario>()
            EditarUsuarioScreen(
                usuarioId = rota.id,
                usuarioViewModel = viewModel(),
                corPostit = corPostit,
                onVoltar = { navController.popBackStack() },
                onExcluiu = { excluiuPropriaConta ->
                    if (excluiuPropriaConta) {
                        // Apagou a própria conta: encerra a sessão e volta ao login.
                        SessaoUsuario.encerrar()
                        navController.irParaLogin("Conta excluída.")
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<Opcoes> {
            OpcoesScreen(opcoesViewModel = opcoesViewModel)
        }
    }
}
