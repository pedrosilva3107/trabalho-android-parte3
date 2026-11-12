package br.edu.up.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.ui.theme.FormatoPostit
import br.edu.up.app.ui.theme.SombraDobra
import br.edu.up.app.ui.theme.TextoPostit

// Componentes reaproveitados em várias telas.

@Composable
fun FaixaTopo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun TituloTela(texto: String, centralizado: Boolean = false) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = if (centralizado) TextAlign.Center else TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Composable
fun PostitCard(
    cor: Color,
    modifier: Modifier = Modifier,
    conteudo: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(6.dp, FormatoPostit)
            .background(cor, FormatoPostit)
    ) {
        conteudo()
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(20.dp)
                .background(SombraDobra, RoundedCornerShape(topStart = 14.dp))
        )
    }
}

@Composable
fun BotaoSalvar(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
        contentPadding = PaddingValues(
            horizontal = 44.dp,
            vertical = 12.dp
        )
    ) {
        Text(text = "✓  $texto", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun BotaoVoltar(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = "‹  Voltar", color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp)
    }
}

// Mensagem de erro/aviso em roxo, embaixo dos formulários.
@Composable
fun Aviso(texto: String?, modifier: Modifier = Modifier) {
    if (!texto.isNullOrEmpty()) {
        Text(
            text = texto,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            modifier = modifier
        )
    }
}

// Post-it grande onde se escreve a nota (telas Criar e Editar).
@Composable
fun CampoNota(
    texto: String,
    onTextoChange: (String) -> Unit,
    corPostit: Color,
    tamanhoFonte: Int
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
            onValueChange = onTextoChange,
            placeholder = { Text("Escreva aqui...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            textStyle = TextStyle(fontSize = tamanhoFonte.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextoPostit,
                unfocusedTextColor = TextoPostit,
                cursorColor = TextoPostit
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Campo de formulário no estilo post-it (login, cadastro e edição de usuário).
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    rotulo: String,
    corPostit: Color,
    modifier: Modifier = Modifier,
    senha: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        singleLine = true,
        visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
        shape = FormatoPostit,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = corPostit,
            unfocusedContainerColor = corPostit,
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = TextoPostit,
            unfocusedTextColor = TextoPostit,
            focusedLabelColor = TextoPostit,
            unfocusedLabelColor = TextoPostit,
            cursorColor = TextoPostit
        ),
        modifier = modifier.fillMaxWidth()
    )
}
