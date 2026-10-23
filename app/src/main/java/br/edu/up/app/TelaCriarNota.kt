package br.edu.up.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaCriarNota(
    paleta: Paleta,
    corPostit: Color,
    tamanhoFonte: Int,
    onSalvar: (String) -> Unit,
    onVoltar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)
        BotaoVoltar(paleta, onVoltar)
        TituloTela("Criar nota", paleta)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PostitCard(
                cor = corPostit,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                TextField(
                    value = texto,
                    onValueChange = {
                        texto = it
                        erro = ""
                    },
                    placeholder = { Text("Escreva aqui...", color = paleta.textoFraco) },
                    textStyle = TextStyle(fontSize = tamanhoFonte.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = paleta.textoPostit,
                        unfocusedTextColor = paleta.textoPostit,
                        cursorColor = paleta.textoPostit
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (erro.isNotEmpty()) {
                Text(
                    text = erro,
                    color = paleta.roxo,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Row(
                modifier = Modifier.padding(top = 28.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                BotaoSalvar(texto = "Salvar", paleta = paleta) {
                    if (texto.isBlank()) {
                        erro = "Escreva algo antes de salvar."
                    } else {
                        onSalvar(texto.trim())
                    }
                }
            }
        }
    }
}
