package br.edu.up.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.app.data.model.Categoria
import br.edu.up.app.ui.theme.FormatoPostit
import br.edu.up.app.ui.theme.SombraDobra
import br.edu.up.app.ui.theme.TextoPostit

// Componentes reaproveitados em várias telas.

// Faixa roxa fina no topo das telas.
@Composable
fun FaixaTopo() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        color = MaterialTheme.colorScheme.primary
    ) {}
}

@Composable
fun TituloTela(texto: String) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

// Cartão post-it das listas (categorias e locais): título em negrito e um texto embaixo.
@Composable
fun CartaoLista(titulo: String, subtitulo: String, cor: Color, onClick: () -> Unit) {
    PostitCard(
        cor = cor,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = titulo, color = TextoPostit, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            if (subtitulo.isNotBlank()) {
                Text(text = subtitulo, color = TextoPostit, fontSize = 14.sp)
            }
        }
    }
}

// Escolha da categoria da nota (mesmo dropdown do formulário feito em aula).
// categoriaId = 0 -> "Sem categoria".
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorCategoria(
    categorias: List<Categoria>,
    categoriaId: Long,
    onEscolher: (Long) -> Unit,
    corPostit: Color
) {
    var expandido by remember { mutableStateOf(false) }
    val nomeEscolhido = categorias.find { it.id == categoriaId }?.nome ?: "Sem categoria"

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = nomeEscolhido,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
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
                focusedTrailingIconColor = TextoPostit,
                unfocusedTrailingIconColor = TextoPostit
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sem categoria") },
                onClick = {
                    onEscolher(0L)
                    expandido = false
                }
            )
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nome) },
                    onClick = {
                        onEscolher(categoria.id)
                        expandido = false
                    }
                )
            }
        }
    }
}

// Cartão no estilo post-it: papel colorido com sombra e a "dobra" no canto
// inferior direito. O conteúdo fica centralizado no cartão.
@Composable
fun PostitCard(
    cor: Color,
    modifier: Modifier = Modifier,
    conteudo: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = FormatoPostit,
        color = cor,
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                conteudo()
            }
            // Dobra do papel
            Surface(
                modifier = Modifier.size(20.dp),
                shape = RoundedCornerShape(topStart = 14.dp),
                color = SombraDobra
            ) {}
        }
    }
}

// Botão "✓ Salvar": cartão creme com sombra; o toque fica no texto (clickable).
@Composable
fun BotaoSalvar(texto: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Text(
            text = "✓  $texto",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 44.dp, vertical = 12.dp)
        )
    }
}

// Botão "+" no canto da tela, na cor do post-it (Criar nota / Novo usuário).
@Composable
fun BotaoAdicionar(cor: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = cor,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "+", color = TextoPostit, fontSize = 28.sp)
        }
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
    if (!texto.isNullOrBlank()) {
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
        // Campo sem borda e sem fundo: quem aparece é o papel do post-it.
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            placeholder = { Text("Escreva aqui...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            textStyle = TextStyle(fontSize = tamanhoFonte.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
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
    senha: Boolean = false,
    numerico: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        singleLine = true,
        visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text
        ),
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
