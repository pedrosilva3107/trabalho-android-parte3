package br.edu.up.app

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Paleta(
    val fundo: Color,
    val texto: Color,
    val textoFraco: Color,
    val roxo: Color,
    val botao: Color,
    val textoPostit: Color
)

val PaletaClara = Paleta(
    fundo = Color(0xFFEEE4B5),
    texto = Color(0xFF1F1B16),
    textoFraco = Color(0xFFA79E80),
    roxo = Color(0xFF7B4C9E),
    botao = Color(0xFFF3EAC6),
    textoPostit = Color(0xFF1F1B16)
)

val PaletaEscura = Paleta(
    fundo = Color(0xFF2B2620),
    texto = Color(0xFFEFE6C6),
    textoFraco = Color(0xFF8A8268),
    roxo = Color(0xFFA171C4),
    botao = Color(0xFF3B352B),
    textoPostit = Color(0xFF1F1B16)
)

val CORES_POSTIT = listOf(
    Color(0xFFE9D07C),
    Color(0xFFC7DDA9),
    Color(0xFFF1B9C9),
    Color(0xFFA9C9E4),
    Color(0xFFF2C79B)
)

val SombraDobra = Color(0x1F000000)

val FormatoPostit = RoundedCornerShape(
    topStart = 14.dp,
    topEnd = 14.dp,
    bottomStart = 14.dp,
    bottomEnd = 2.dp
)
