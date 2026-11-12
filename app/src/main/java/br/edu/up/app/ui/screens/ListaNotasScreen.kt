package br.edu.up.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.data.model.Nota
import br.edu.up.app.data.model.Usuario
import br.edu.up.app.ui.components.PostitCard
import br.edu.up.app.ui.theme.TextoPostit
import br.edu.up.app.ui.viewmodel.NotaViewModel

// Read do CRUD de notas.
@Composable
fun ListaNotasScreen(
    notaViewModel: NotaViewModel,
    usuario: Usuario,
    corPostit: Color,
    tamanhoFonte: Int,
    onCriar: () -> Unit,
    onAbrir: (Long) -> Unit
) {
    // Busca as notas no banco ao abrir a tela (e de novo se o usuário mudar).
    LaunchedEffect(usuario.id) {
        notaViewModel.listar()
    }

    val notas = notaViewModel.notas

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Notas",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Olá, ${usuario.nome}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = "${notas.size} notas",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            if (notas.isEmpty()) {
                Text(
                    text = "Nenhuma nota ainda. Toque no + para criar a primeira.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            } else {
                val totalLinhas = (notas.size + 1) / 2

                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(totalLinhas) { linha ->
                        val esquerda = linha * 2
                        val direita = esquerda + 1

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            CartaoNota(
                                nota = notas[esquerda],
                                cor = corPostit,
                                tamanhoFonte = tamanhoFonte,
                                modifier = Modifier.weight(1f),
                                onClick = { onAbrir(notas[esquerda].id) }
                            )
                            if (direita < notas.size) {
                                CartaoNota(
                                    nota = notas[direita],
                                    cor = corPostit,
                                    tamanhoFonte = tamanhoFonte,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onAbrir(notas[direita].id) }
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onCriar,
            containerColor = corPostit,
            contentColor = TextoPostit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text(text = "+", fontSize = 28.sp)
        }
    }
}

@Composable
private fun CartaoNota(
    nota: Nota,
    cor: Color,
    tamanhoFonte: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PostitCard(
        cor = cor,
        modifier = modifier
            .height(150.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = nota.texto,
            color = TextoPostit,
            fontSize = tamanhoFonte.sp,
            textAlign = TextAlign.Center,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(12.dp)
        )
    }
}
