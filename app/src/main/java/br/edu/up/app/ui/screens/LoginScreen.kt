package br.edu.up.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.ui.components.Aviso
import br.edu.up.app.ui.components.BotaoSalvar
import br.edu.up.app.ui.components.CampoTexto
import br.edu.up.app.ui.components.FaixaTopo
import br.edu.up.app.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    corPostit: Color,
    mensagem: String,
    onEntrou: () -> Unit,
    onCadastrar: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Quando o login dá certo, o ViewModel marca "concluido" e a tela navega.
    LaunchedEffect(authViewModel.concluido) {
        if (authViewModel.concluido) {
            authViewModel.consumirResultado()
            onEntrou()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FaixaTopo()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notas",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Entre para acessar suas notas",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            CampoTexto(
                valor = usuario,
                onValorChange = { usuario = it },
                rotulo = "Usuário",
                corPostit = corPostit
            )

            CampoTexto(
                valor = senha,
                onValorChange = { senha = it },
                rotulo = "Senha",
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )

            // Erro do login tem prioridade sobre o aviso que veio da navegação.
            Aviso(
                texto = authViewModel.mensagem ?: mensagem,
                modifier = Modifier.padding(top = 10.dp)
            )

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Entrar") {
                    authViewModel.entrar(usuario, senha)
                }
            }

            TextButton(
                onClick = {
                    authViewModel.limparMensagem()
                    onCadastrar()
                },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(text = "Não tem conta? Cadastre-se", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
