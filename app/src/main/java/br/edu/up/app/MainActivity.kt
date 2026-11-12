package br.edu.up.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.navigation.AppNavigation
import br.edu.up.app.navigation.Menu
import br.edu.up.app.navigation.ehPublica
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

                // Menu só com sessão ativa e fora das telas públicas.
                val mostrarMenu = SessaoUsuario.usuario != null &&
                        destino != null && !destino.ehPublica()

                Scaffold(
                    bottomBar = {
                        if (mostrarMenu) Menu(navController = navController, destino = destino)
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
    }
}
