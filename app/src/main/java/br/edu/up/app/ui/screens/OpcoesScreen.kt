package br.edu.up.app.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.ui.components.PostitCard
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
    ) {
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
                    color = TextoPostit
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                CORES_POSTIT.forEachIndexed { i, cor ->
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { opcoesViewModel.escolherCor(i) },
                        shape = RoundedCornerShape(50),
                        color = corTexto
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(if (i == corPostitIndex) 3.dp else 1.dp),
                            shape = RoundedCornerShape(50),
                            color = cor
                        ) {}
                    }
                }
            }

            Rotulo("TAMANHO DA FONTE", Modifier.padding(top = 32.dp))

            val podeDiminuir = tamanhoFonte > 12
            val podeAumentar = tamanhoFonte < 24
            val corApagada = corTexto.copy(alpha = 0.38f)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                TextButton(
                    onClick = { opcoesViewModel.diminuirFonte() },
                    enabled = podeDiminuir
                ) {
                    Text("−", fontSize = 18.sp, color = if (podeDiminuir) corTexto else corApagada)
                }
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${tamanhoFonte}sp",
                        color = corTexto,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = { opcoesViewModel.aumentarFonte() },
                    enabled = podeAumentar
                ) {
                    Text("+", fontSize = 18.sp, color = if (podeAumentar) corTexto else corApagada)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Modo escuro",
                    color = corTexto,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = { opcoesViewModel.alternarModoEscuro() }) {
                    Text(if (modoEscuro) "Ligado" else "Desligado", color = corTexto)
                }
            }
        }
    }
}

@Composable
private fun Rotulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
