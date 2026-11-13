package br.edu.up.app.data.model

// Model: uma nota da tabela "notas".
// categoriaId = 0 quando a nota não tem categoria.
data class Nota(
    val id: Long,
    val usuarioId: Long,
    val texto: String,
    val categoriaId: Long
)
