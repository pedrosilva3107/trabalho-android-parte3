package br.edu.up.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.navigation.ALTURA_MENU
import br.edu.up.app.navigation.AppNavigation
import br.edu.up.app.navigation.Cadastro
import br.edu.up.app.navigation.Login
import br.edu.up.app.navigation.Menu
import br.edu.up.app.ui.theme.NotasAppTheme
import br.edu.up.app.ui.viewmodel.OpcoesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Criado aqui (fora do NavHost) para valer no app inteiro.
            val opcoesViewModel: OpcoesViewModel = viewModel()

            NotasAppTheme(darkTheme = opcoesViewModel.modoEscuro) {
                val navController = rememberNavController()
                val entradaAtual by navController.currentBackStackEntryAsState()
                val destino = entradaAtual?.destination

                // Menu só com sessão ativa e fora das telas públicas (Login e Cadastro).
                val telaPublica = (destino?.hasRoute<Login>() ?: false) ||
                        (destino?.hasRoute<Cadastro>() ?: false)
                val mostrarMenu = SessaoUsuario.usuario != null &&
                        destino != null && !telaPublica

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // O menu fica no rodapé, por cima das telas; as telas ganham
                    // embaixo um espaço do tamanho do menu para nada ficar escondido.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        AppNavigation(
                            navController = navController,
                            opcoesViewModel = opcoesViewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = if (mostrarMenu) ALTURA_MENU else 0.dp)
                        )
                        if (mostrarMenu) Menu(navController = navController)
                    }
                }
            }
        }
    }
}
