package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.viewmodel.OpcoesViewModel

@Composable
fun TelaOpcoes(
    opcoesViewModel: OpcoesViewModel,
    paleta: Paleta,
    corPostit: Color
) {
    val corPostitIndex = opcoesViewModel.corPostitIndex
    val tamanhoFonte = opcoesViewModel.tamanhoFonte
    val modoEscuro = opcoesViewModel.modoEscuro

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)
        TituloTela("Opções", paleta, centralizado = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Rotulo("COR DO POST-IT", paleta)

            PostitCard(
                cor = corPostit,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(width = 150.dp, height = 120.dp)
            ) {
                Text(
                    text = "Exemplo",
                    color = paleta.textoPostit,
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
                                color = paleta.texto,
                                shape = CircleShape
                            )
                            .clickable { opcoesViewModel.escolherCor(i) }
                    )
                }
            }

            Rotulo("TAMANHO DA FONTE", paleta, topo = 32.dp)

            val coresBotao = ButtonDefaults.outlinedButtonColors(contentColor = paleta.texto)

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
                    color = paleta.texto,
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
                    color = paleta.texto,
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
private fun Rotulo(texto: String, paleta: Paleta, topo: androidx.compose.ui.unit.Dp = 0.dp) {
    Text(
        text = texto,
        color = paleta.textoFraco,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = topo)
    )
}
