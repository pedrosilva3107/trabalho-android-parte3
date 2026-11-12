package br.edu.up.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import br.edu.up.app.ui.components.FaixaTopo
import br.edu.up.app.ui.components.PostitCard
import br.edu.up.app.ui.components.TituloTela
import br.edu.up.app.ui.theme.CORES_POSTIT
import br.edu.up.app.ui.theme.TextoPostit
import br.edu.up.app.ui.viewmodel.OpcoesViewModel

@Composable
fun OpcoesScreen(opcoesViewModel: OpcoesViewModel) {
    val corPostitIndex = opcoesViewModel.corPostitIndex
    val tamanhoFonte = opcoesViewModel.tamanhoFonte
    val modoEscuro = opcoesViewModel.modoEscuro
    val corTexto = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FaixaTopo()
        TituloTela("Opções", centralizado = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Rotulo("COR DO POST-IT")

            PostitCard(
                cor = CORES_POSTIT[corPostitIndex],
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(width = 150.dp, height = 120.dp)
            ) {
                Text(
                    text = "Exemplo",
                    color = TextoPostit,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                for (i in CORES_POSTIT.indices) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CORES_POSTIT[i], CircleShape)
                            .border(
                                width = if (i == corPostitIndex) 3.dp else 1.dp,
                                color = corTexto,
                                shape = CircleShape
                            )
                            .clickable { opcoesViewModel.escolherCor(i) }
                    )
                }
            }

            Rotulo("TAMANHO DA FONTE", topo = 32.dp)

            val coresBotao = ButtonDefaults.outlinedButtonColors(contentColor = corTexto)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                OutlinedButton(
                    onClick = { opcoesViewModel.diminuirFonte() },
                    enabled = tamanhoFonte > 12,
                    colors = coresBotao
                ) {
                    Text("−", fontSize = 18.sp)
                }
                Text(
                    text = "${tamanhoFonte}sp",
                    color = corTexto,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(90.dp)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = { opcoesViewModel.aumentarFonte() },
                    enabled = tamanhoFonte < 24,
                    colors = coresBotao
                ) {
                    Text("+", fontSize = 18.sp)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                Text(
                    text = "Modo escuro",
                    color = corTexto,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = { opcoesViewModel.alternarModoEscuro() }, colors = coresBotao) {
                    Text(if (modoEscuro) "Ligado" else "Desligado")
                }
            }
        }
    }
}

@Composable
private fun Rotulo(texto: String, topo: Dp = 0.dp) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = topo)
    )
}
