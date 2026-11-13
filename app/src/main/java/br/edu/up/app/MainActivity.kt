package br.edu.up.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
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
                Menu(opcoesViewModel = opcoesViewModel)
            }
        }
    }
}
