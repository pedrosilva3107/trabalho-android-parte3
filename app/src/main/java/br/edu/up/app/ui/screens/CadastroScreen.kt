package br.edu.up.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.up.app.ui.components.Aviso
import br.edu.up.app.ui.components.BotaoSalvar
import br.edu.up.app.ui.components.BotaoVoltar
import br.edu.up.app.ui.components.CampoTexto
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.viewmodel.AuthViewModel

// Create do CRUD de usuários. Usada no "Criar conta" (sem login)
// e no "Novo usuário" (dentro do app).
@Composable
fun CadastroScreen(
    authViewModel: AuthViewModel,
    corPostit: Color,
    titulo: String,
    onConcluido: () -> Unit,
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacao by remember { mutableStateOf("") }

    LaunchedEffect(authViewModel.concluido) {
        if (authViewModel.concluido) {
            authViewModel.consumirResultado()
            onConcluido()
        }
    }

    // imePadding + verticalScroll: com o teclado aberto dá para rolar até o botão Salvar.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        BotaoVoltar(onVoltar)
        TituloTela(titulo)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = nome,
                onValorChange = { nome = it },
                rotulo = "Nome",
                corPostit = corPostit
            )
            CampoTexto(
                valor = usuario,
                onValorChange = { usuario = it },
                rotulo = "Usuário",
                corPostit = corPostit,
                modifier = Modifier.padding(top = 14.dp)
            )
            CampoTexto(
                valor = senha,
                onValorChange = { senha = it },
                rotulo = "Senha",
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )
            CampoTexto(
                valor = confirmacao,
                onValorChange = { confirmacao = it },
                rotulo = "Confirmar senha",
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )

            Aviso(authViewModel.mensagem, Modifier.padding(top = 10.dp))

            Column(modifier = Modifier.padding(top = 28.dp, bottom = 24.dp)) {
                BotaoSalvar(texto = "Cadastrar") {
                    authViewModel.cadastrar(nome, usuario, senha, confirmacao)
                }
            }
        }
    }
}
