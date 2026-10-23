package br.edu.up.app.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.viewmodel.UsuarioViewModel

// Update e Delete do CRUD de usuários.
@Composable
fun TelaEditarUsuario(
    usuarioViewModel: UsuarioViewModel,
    paleta: Paleta,
    corPostit: Color,
    onVoltar: () -> Unit,
    onExcluiu: (excluiuPropriaConta: Boolean) -> Unit
) {
    val usuario = usuarioViewModel.usuarioSelecionado ?: return
    val ehVoce = usuario.id == usuarioViewModel.usuarioLogado?.id

    var nome by remember { mutableStateOf(usuario.nome) }
    var login by remember { mutableStateOf(usuario.login) }
    var novaSenha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }
    var confirmarExclusao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)
        BotaoVoltar(paleta, onVoltar)
        TituloTela("Editar usuário", paleta)

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
                valor = login,
                onValorChange = { login = it },
                rotulo = "Usuário",
                paleta = paleta,
                corPostit = corPostit,
                modifier = Modifier.padding(top = 14.dp)
            )
            CampoTexto(
                valor = novaSenha,
                onValorChange = { novaSenha = it },
                rotulo = "Nova senha (deixe vazio para manter)",
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

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Salvar", paleta = paleta) {
                    val falha = usuarioViewModel.atualizar(usuario, nome, login, novaSenha)
                    if (falha == null) {
                        onVoltar()
                    } else {
                        erro = falha
                    }
                }
            }

            // Primeiro toque pede confirmação; o segundo exclui.
            TextButton(
                onClick = {
                    if (confirmarExclusao) {
                        usuarioViewModel.deletar(usuario)
                        onExcluiu(ehVoce)
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
                Text(text = texto, color = paleta.roxo)
            }
        }
    }
}
