package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

// Campo de formulário no estilo post-it (login, cadastro e edição de usuário).
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    rotulo: String,
    paleta: Paleta,
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
            focusedBorderColor = paleta.texto,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = paleta.textoPostit,
            unfocusedTextColor = paleta.textoPostit,
            focusedLabelColor = paleta.textoPostit,
            unfocusedLabelColor = paleta.textoPostit,
            cursorColor = paleta.textoPostit
        ),
        modifier = modifier.fillMaxWidth()
    )
}

// Menu inferior. Só aparece com usuário logado (nunca no login/cadastro).
@Composable
fun MenuInferior(
    telaAtual: Tela,
    paleta: Paleta,
    onNotas: () -> Unit,
    onUsuarios: () -> Unit,
    onOpcoes: () -> Unit,
    onSair: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(paleta.botao)) {
        FaixaTopo(paleta)
        Row(modifier = Modifier.fillMaxWidth()) {
            ItemMenu(
                texto = "Notas",
                selecionado = telaAtual == Tela.LISTA || telaAtual == Tela.CRIAR || telaAtual == Tela.EDITAR,
                paleta = paleta,
                modifier = Modifier.weight(1f),
                onClick = onNotas
            )
            ItemMenu(
                texto = "Usuários",
                selecionado = telaAtual == Tela.USUARIOS || telaAtual == Tela.NOVO_USUARIO || telaAtual == Tela.EDITAR_USUARIO,
                paleta = paleta,
                modifier = Modifier.weight(1f),
                onClick = onUsuarios
            )
            ItemMenu(
                texto = "Opções",
                selecionado = telaAtual == Tela.OPCOES,
                paleta = paleta,
                modifier = Modifier.weight(1f),
                onClick = onOpcoes
            )
            ItemMenu(
                texto = "Sair",
                selecionado = false,
                paleta = paleta,
                modifier = Modifier.weight(1f),
                onClick = onSair
            )
        }
    }
}

@Composable
private fun ItemMenu(
    texto: String,
    selecionado: Boolean,
    paleta: Paleta,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = texto,
            color = if (selecionado) paleta.roxo else paleta.texto,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}
