package br.edu.up.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.up.app.ui.components.Aviso
import br.edu.up.app.ui.components.BotaoSalvar
import br.edu.up.app.ui.components.BotaoVoltar
import br.edu.up.app.ui.components.CampoNota
import br.edu.up.app.ui.components.FaixaTopo
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.viewmodel.NotaViewModel

// Create do CRUD de notas.
@Composable
fun CriarNotaScreen(
    notaViewModel: NotaViewModel,
    corPostit: Color,
    tamanhoFonte: Int,
    onVoltar: () -> Unit
) {
    LaunchedEffect(notaViewModel.salvo) {
        if (notaViewModel.salvo) {
            notaViewModel.consumirSalvo()
            onVoltar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FaixaTopo()
        BotaoVoltar(onVoltar)
        TituloTela("Criar nota")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoNota(
                texto = notaViewModel.texto,
                onTextoChange = notaViewModel::mudarTexto,
                corPostit = corPostit,
                tamanhoFonte = tamanhoFonte
            )

            Aviso(notaViewModel.mensagem, Modifier.padding(top = 12.dp))

            Row(
                modifier = Modifier.padding(top = 28.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                BotaoSalvar(texto = "Salvar") {
                    notaViewModel.criar()
                }
            }
        }
    }
}
