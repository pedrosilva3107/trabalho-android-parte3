package br.edu.up.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.ui.components.BotaoAdicionar
import br.edu.up.app.ui.components.CartaoLista
import br.edu.up.app.ui.viewmodel.CategoriaViewModel

// Read do CRUD de categorias.
@Composable
fun CategoriasScreen(
    categoriaViewModel: CategoriaViewModel,
    corPostit: Color,
    onNova: () -> Unit,
    onAbrir: (Long) -> Unit
) {
    // Busca as categorias no banco ao abrir a tela.
    LaunchedEffect(Unit) {
        categoriaViewModel.listar()
    }

    val categorias = categoriaViewModel.categorias

    // contentAlignment = BottomEnd deixa o botão + no canto de baixo à direita.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = if (categorias.size == 1) "1 categoria" else "${categorias.size} categorias",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(20.dp)
            )

            if (categorias.isEmpty()) {
                Text(
                    text = "Nenhuma categoria ainda. Toque no + para criar (ex.: Faculdade, Mercado).",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categorias, key = { it.id }) { categoria ->
                    CartaoLista(
                        titulo = categoria.nome,
                        subtitulo = "",
                        cor = corPostit,
                        onClick = { onAbrir(categoria.id) }
                    )
                }

                // Espaço no fim da lista para o último cartão não ficar embaixo do botão +.
                item { Spacer(modifier = Modifier.height(88.dp)) }
            }
        }

        BotaoAdicionar(
            cor = corPostit,
            onClick = onNova,
            modifier = Modifier.padding(24.dp)
        )
    }
}
