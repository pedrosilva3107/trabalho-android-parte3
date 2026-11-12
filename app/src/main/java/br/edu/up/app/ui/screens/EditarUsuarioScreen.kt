package br.edu.up.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import br.edu.up.app.ui.components.Aviso
import br.edu.up.app.ui.components.BotaoSalvar
import br.edu.up.app.ui.components.BotaoVoltar
import br.edu.up.app.ui.components.CampoTexto
import br.edu.up.app.ui.components.FaixaTopo
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.viewmodel.UsuarioViewModel

// Update e Delete do CRUD de usuários. Recebe o id pela rota EditarUsuario(id).
@Composable
fun EditarUsuarioScreen(
    usuarioId: Long,
    usuarioViewModel: UsuarioViewModel,
    corPostit: Color,
    onVoltar: () -> Unit,
    onExcluiu: (excluiuPropriaConta: Boolean) -> Unit
) {
    LaunchedEffect(usuarioId) { usuarioViewModel.buscarPorId(usuarioId) }
    LaunchedEffect(usuarioViewModel.salvo) {
        if (usuarioViewModel.salvo) {
            usuarioViewModel.consumirSalvo()
            onVoltar()
        }
    }
    LaunchedEffect(usuarioViewModel.excluido) {
        if (usuarioViewModel.excluido) {
            onExcluiu(usuarioViewModel.excluiuPropriaConta)
        }
    }

    val ehVoce = usuarioId == usuarioViewModel.usuarioLogadoId
    var novaSenha by remember { mutableStateOf("") }
    var confirmarExclusao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FaixaTopo()
        BotaoVoltar(onVoltar)
        TituloTela("Editar usuário")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = usuarioViewModel.nome,
                onValorChange = usuarioViewModel::mudarNome,
                rotulo = "Nome",
                corPostit = corPostit
            )
            CampoTexto(
                valor = usuarioViewModel.login,
                onValorChange = usuarioViewModel::mudarLogin,
                rotulo = "Usuário",
                corPostit = corPostit,
                modifier = Modifier.padding(top = 14.dp)
            )
            CampoTexto(
                valor = novaSenha,
                onValorChange = { novaSenha = it },
                rotulo = "Nova senha (deixe vazio para manter)",
                corPostit = corPostit,
                senha = true,
                modifier = Modifier.padding(top = 14.dp)
            )

            Aviso(usuarioViewModel.mensagem, Modifier.padding(top = 10.dp))

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Salvar") {
                    usuarioViewModel.salvar(novaSenha)
                }
            }

            // Primeiro toque pede confirmação; o segundo exclui.
            TextButton(
                onClick = {
                    if (confirmarExclusao) {
                        usuarioViewModel.excluir()
                    } else {
                        confirmarExclusao = true
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                val texto = when {
                    !confirmarExclusao -> if (ehVoce) "Excluir minha conta" else "Excluir usuário"
                    ehVoce -> "Toque de novo: apaga sua conta, suas notas e sai do app"
                    else -> "Toque de novo para confirmar (as notas dele também serão apagadas)"
                }
                Text(text = texto, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
