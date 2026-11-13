package br.edu.up.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.data.model.Usuario
import br.edu.up.app.ui.components.BotaoAdicionar
import br.edu.up.app.ui.components.PostitCard
import br.edu.up.app.ui.theme.TextoPostit
import br.edu.up.app.ui.viewmodel.UsuarioViewModel

@Composable
fun UsuariosScreen(
    usuarioViewModel: UsuarioViewModel,
    corPostit: Color,
    onNovo: () -> Unit,
    onAbrir: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        usuarioViewModel.listar()
    }

    val usuarios = usuarioViewModel.usuarios
    val idLogado = usuarioViewModel.usuarioLogadoId()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = if (usuarios.size == 1) "1 cadastrado" else "${usuarios.size} cadastrados",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(20.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(usuarios, key = { it.id }) { usuario ->
                    CartaoUsuario(
                        usuario = usuario,
                        ehVoce = usuario.id == idLogado,
                        cor = corPostit,
                        onClick = { onAbrir(usuario.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(88.dp)) }
            }
        }

        BotaoAdicionar(
            cor = corPostit,
            onClick = onNovo,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
private fun CartaoUsuario(
    usuario: Usuario,
    ehVoce: Boolean,
    cor: Color,
    onClick: () -> Unit
) {
    PostitCard(
        cor = cor,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (ehVoce) "${usuario.nome} (você)" else usuario.nome,
                color = TextoPostit,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${usuario.login}",
                color = TextoPostit,
                fontSize = 14.sp
            )
        }
    }
}
