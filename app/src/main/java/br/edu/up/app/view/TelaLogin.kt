package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import br.edu.up.app.viewmodel.UsuarioViewModel

@Composable
fun TelaLogin(
    usuarioViewModel: UsuarioViewModel,
    paleta: Paleta,
    corPostit: Color,
    mensagem: String,
    onEntrou: () -> Unit,
    onCadastrar: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notas",
                color = paleta.texto,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Entre para acessar suas notas",
                color = paleta.textoFraco,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            CampoTexto(
                valor = usuario,
                onValorChange = { usuario = it },
                rotulo = "Usuário",
                paleta = paleta,
                corPostit = corPostit
            )

            CampoTexto(
                valor = senha,
                onValorChange = { senha = it },
                rotulo = "Senha",
                paleta = paleta,
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )

            val aviso = if (erro.isNotEmpty()) erro else mensagem
            if (aviso.isNotEmpty()) {
                Text(
                    text = aviso,
                    color = paleta.roxo,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Entrar", paleta = paleta) {
                    val falha = usuarioViewModel.entrar(usuario, senha)
                    if (falha == null) {
                        erro = ""
                        onEntrou()
                    } else {
                        erro = falha
                    }
                }
            }

            TextButton(
                onClick = onCadastrar,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(text = "Não tem conta? Cadastre-se", color = paleta.roxo)
            }
        }
    }
}
