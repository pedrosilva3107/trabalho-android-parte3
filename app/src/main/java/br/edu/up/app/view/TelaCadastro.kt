package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.viewmodel.UsuarioViewModel

// Create do CRUD de usuários. Usada no "Criar conta" (sem login)
// e no "Novo usuário" (dentro do app).
@Composable
fun TelaCadastro(
    usuarioViewModel: UsuarioViewModel,
    paleta: Paleta,
    corPostit: Color,
    titulo: String,
    onConcluido: () -> Unit,
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacao by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)
        BotaoVoltar(paleta, onVoltar)
        TituloTela(titulo, paleta)

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
                paleta = paleta,
                corPostit = corPostit
            )
            CampoTexto(
                valor = usuario,
                onValorChange = { usuario = it },
                rotulo = "Usuário",
                paleta = paleta,
                corPostit = corPostit,
                modifier = Modifier.padding(top = 14.dp)
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
            CampoTexto(
                valor = confirmacao,
                onValorChange = { confirmacao = it },
                rotulo = "Confirmar senha",
                paleta = paleta,
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )

            if (erro.isNotEmpty()) {
                Text(
                    text = erro,
                    color = paleta.roxo,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Column(modifier = Modifier.padding(top = 28.dp, bottom = 24.dp)) {
                BotaoSalvar(texto = "Cadastrar", paleta = paleta) {
                    val falha = usuarioViewModel.cadastrar(nome, usuario, senha, confirmacao)
                    if (falha == null) {
                        onConcluido()
                    } else {
                        erro = falha
                    }
                }
            }
        }
    }
}
