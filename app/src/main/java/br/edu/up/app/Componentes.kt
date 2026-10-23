package br.edu.up.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FaixaTopo(paleta: Paleta) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(paleta.roxo)
    )
}

@Composable
fun TituloTela(texto: String, paleta: Paleta, centralizado: Boolean = false) {
    Text(
        text = texto,
        color = paleta.texto,
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
fun BotaoSalvar(texto: String, paleta: Paleta, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = paleta.botao,
            contentColor = paleta.texto
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
fun BotaoVoltar(paleta: Paleta, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = "‹  Voltar", color = paleta.texto, fontSize = 15.sp)
    }
}
