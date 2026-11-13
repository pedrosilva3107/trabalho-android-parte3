package br.edu.up.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import br.edu.up.app.data.model.Nota
import br.edu.up.app.data.model.Usuario
import br.edu.up.app.ui.components.BotaoAdicionar
import br.edu.up.app.ui.components.PostitCard
import br.edu.up.app.ui.theme.TextoPostit
import br.edu.up.app.ui.viewmodel.NotaViewModel

@Composable
fun ListaNotasScreen(
    notaViewModel: NotaViewModel,
    usuario: Usuario,
    corPostit: Color,
    tamanhoFonte: Int,
    onCriar: () -> Unit,
    onAbrir: (Long) -> Unit
) {
    LaunchedEffect(usuario.id) {
        notaViewModel.listar()
    }

    val notas = notaViewModel.notas

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Olá, ${usuario.nome}",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 72.dp)
                )
                Text(
                    text = if (notas.size == 1) "1 nota" else "${notas.size} notas",
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
                val linhas = List((notas.size + 1) / 2) { indice -> indice }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(linhas) { linha ->
                        val esquerda = linha * 2
                        val direita = esquerda + 1

                        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                            CartaoNota(
                                nota = notas[esquerda],
                                categoria = notaViewModel.nomeCategoria(notas[esquerda].categoriaId),
                                cor = corPostit,
                                tamanhoFonte = tamanhoFonte,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(end = 8.dp),
                                onClick = { onAbrir(notas[esquerda].id) }
                            )
                            if (direita < notas.size) {
                                CartaoNota(
                                    nota = notas[direita],
                                    categoria = notaViewModel.nomeCategoria(notas[direita].categoriaId),
                                    cor = corPostit,
                                    tamanhoFonte = tamanhoFonte,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp),
                                    onClick = { onAbrir(notas[direita].id) }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(84.dp)) }
                }
            }
        }

        BotaoAdicionar(
            cor = corPostit,
            onClick = onCriar,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
private fun CartaoNota(
    nota: Nota,
    categoria: String,
    cor: Color,
    tamanhoFonte: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val espaco = if (categoria.isEmpty()) 16000 else 12000
    val limite = espaco / (tamanhoFonte * tamanhoFonte)
    val texto = if (nota.texto.length > limite) nota.texto.take(limite).trim() + "..." else nota.texto

    PostitCard(
        cor = cor,
        modifier = modifier
            .height(150.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (categoria.isNotEmpty()) {
                Text(
                    text = categoria.uppercase(),
                    color = TextoPostit,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = texto,
                color = TextoPostit,
                fontSize = tamanhoFonte.sp
            )
        }
    }
}
