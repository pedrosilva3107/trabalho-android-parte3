package br.edu.up.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.viewmodel.CategoriaViewModel

// Create, Update e Delete do CRUD de categorias.
// Recebe o id pela rota CategoriaForm(id): 0 = nova categoria.
@Composable
fun CategoriaFormScreen(
    categoriaId: Long,
    categoriaViewModel: CategoriaViewModel,
    corPostit: Color,
    onVoltar: () -> Unit
) {
    val editando = categoriaId != 0L

    LaunchedEffect(categoriaId) {
        if (editando) categoriaViewModel.buscarPorId(categoriaId)
    }
    LaunchedEffect(categoriaViewModel.salvo) {
        if (categoriaViewModel.salvo) {
            categoriaViewModel.consumirSalvo()
            onVoltar()
        }
    }

    var confirmarExclusao by remember { mutableStateOf(false) }

    // imePadding + verticalScroll: com o teclado aberto dá para rolar até o botão Salvar.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        BotaoVoltar(onVoltar)
        TituloTela(if (editando) "Editar categoria" else "Nova categoria")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = categoriaViewModel.nome,
                onValorChange = categoriaViewModel::mudarNome,
                rotulo = "Nome da categoria",
                corPostit = corPostit
            )

            Aviso(categoriaViewModel.mensagem, Modifier.padding(top = 10.dp))

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Salvar") {
                    categoriaViewModel.salvar()
                }
            }

            if (editando) {
                // Primeiro toque pede confirmação; o segundo exclui.
                TextButton(
                    onClick = {
                        if (confirmarExclusao) {
                            categoriaViewModel.excluir()
                        } else {
                            confirmarExclusao = true
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = if (confirmarExclusao) "Toque de novo para confirmar (as notas ficam sem categoria)"
                        else "Excluir categoria",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
