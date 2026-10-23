package br.edu.up.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaLogin(
    paleta: Paleta,
    corPostit: Color,
    onEntrar: (String) -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
    ) {
        FaixaTopo(paleta)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notas",
                color = paleta.texto,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Entre para acessar suas notas",
                color = paleta.textoFraco,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            val coresCampo = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = corPostit,
                unfocusedContainerColor = corPostit,
                focusedBorderColor = paleta.texto,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = paleta.textoPostit,
                unfocusedTextColor = paleta.textoPostit,
                focusedLabelColor = paleta.textoPostit,
                unfocusedLabelColor = paleta.textoPostit,
                cursorColor = paleta.textoPostit
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuário") },
                singleLine = true,
                shape = FormatoPostit,
                colors = coresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = FormatoPostit,
                colors = coresCampo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            )

            if (erro.isNotEmpty()) {
                Text(
                    text = erro,
                    color = paleta.roxo,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Column(modifier = Modifier.padding(top = 28.dp)) {
                BotaoSalvar(texto = "Entrar", paleta = paleta) {
                    if (usuario.isBlank() || senha.isBlank()) {
                        erro = "Preencha usuário e senha."
                    } else {
                        erro = ""
                        onEntrar(usuario.trim())
                    }
                }
            }
        }
    }
}
