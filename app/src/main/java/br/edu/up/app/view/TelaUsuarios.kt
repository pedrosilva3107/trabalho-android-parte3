package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.model.Usuario
import br.edu.up.app.viewmodel.UsuarioViewModel

// Read do CRUD de usuários.
@Composable
fun TelaUsuarios(
    usuarioViewModel: UsuarioViewModel,
    paleta: Paleta,
    corPostit: Color,
    onNovo: () -> Unit,
    onAbrir: (Usuario) -> Unit
) {
    // Busca os usuários no banco ao abrir a tela.
    LaunchedEffect(Unit) {
        usuarioViewModel.carregarUsuarios()
    }

    val usuarios = usuarioViewModel.usuarios
    val idLogado = usuarioViewModel.usuarioLogado?.id

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Usuários",
                    color = paleta.texto,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${usuarios.size} cadastrados",
                    color = paleta.textoFraco,
                    fontSize = 13.sp
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(usuarios) { usuario ->
                    CartaoUsuario(
                        usuario = usuario,
                        ehVoce = usuario.id == idLogado,
                        cor = corPostit,
                        paleta = paleta,
                        onClick = { onAbrir(usuario) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onNovo,
            containerColor = corPostit,
            contentColor = paleta.textoPostit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text(text = "+", fontSize = 28.sp)
        }
    }
}

@Composable
private fun CartaoUsuario(
    usuario: Usuario,
    ehVoce: Boolean,
    cor: Color,
    paleta: Paleta,
    onClick: () -> Unit
) {
    PostitCard(
        cor = cor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (ehVoce) "${usuario.nome} (você)" else usuario.nome,
                color = paleta.textoPostit,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${usuario.login}",
                color = paleta.textoPostit,
                fontSize = 14.sp
            )
        }
    }
}
