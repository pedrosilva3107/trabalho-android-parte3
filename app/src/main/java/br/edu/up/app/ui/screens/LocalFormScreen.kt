package br.edu.up.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.edu.up.app.ui.components.Aviso
import br.edu.up.app.ui.components.BotaoSalvar
import br.edu.up.app.ui.components.BotaoVoltar
import br.edu.up.app.ui.components.CampoTexto
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.viewmodel.LocalViewModel

// Create, Update e Delete do CRUD de locais.
// Recebe o id pela rota LocalForm(id): 0 = novo local.
// O endereço vem da API ViaCEP (Retrofit) e o botão "Abrir no mapa" usa o app de mapas do celular.
@Composable
fun LocalFormScreen(
    localId: Long,
    localViewModel: LocalViewModel,
    corPostit: Color,
    onVoltar: () -> Unit
) {
    val editando = localId != 0L
    val contexto = LocalContext.current

    LaunchedEffect(localId) {
        if (editando) localViewModel.buscarPorId(localId)
    }
    LaunchedEffect(localViewModel.salvo) {
        if (localViewModel.salvo) {
            localViewModel.consumirSalvo()
            onVoltar()
        }
    }

    var confirmarExclusao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        BotaoVoltar(onVoltar)
        TituloTela(if (editando) "Editar local" else "Novo local")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = localViewModel.nome,
                onValorChange = localViewModel::mudarNome,
                rotulo = "Nome do local (ex.: Faculdade)",
                corPostit = corPostit
            )
            CampoTexto(
                valor = localViewModel.cep,
                onValorChange = localViewModel::mudarCep,
                rotulo = "CEP",
                corPostit = corPostit,
                numerico = true,
                modifier = Modifier.padding(top = 14.dp)
            )
            TextButton(
                onClick = { localViewModel.buscarCep() },
                enabled = !localViewModel.carregando
            ) {
                Text("Buscar endereço pelo CEP", color = MaterialTheme.colorScheme.primary)
            }
            if (localViewModel.carregando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            CampoTexto(
                valor = localViewModel.endereco,
                onValorChange = localViewModel::mudarEndereco,
                rotulo = "Endereço",
                corPostit = corPostit,
                modifier = Modifier.padding(top = 6.dp)
            )

            Aviso(localViewModel.mensagem, Modifier.padding(top = 10.dp))

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Salvar") {
                    localViewModel.salvar()
                }
            }

            // Abre o endereço no app de mapas do celular.
            if (localViewModel.endereco.isNotBlank()) {
                TextButton(
                    onClick = {
                        val busca = localViewModel.endereco.replace(" ", "+")
                        val mapa = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$busca"))
                        try {
                            contexto.startActivity(mapa)
                        } catch (_: Exception) {
                            Toast.makeText(contexto, "Não foi possível abrir o mapa", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Abrir no mapa", color = MaterialTheme.colorScheme.primary)
                }
            }

            if (editando) {
                // Primeiro toque pede confirmação; o segundo exclui.
                TextButton(
                    onClick = {
                        if (confirmarExclusao) {
                            localViewModel.excluir()
                        } else {
                            confirmarExclusao = true
                        }
                    }
                ) {
                    Text(
                        text = if (confirmarExclusao) "Toque de novo para confirmar" else "Excluir local",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
