package br.edu.up.app.data.model

// Model: uma nota da tabela "notas".
data class Nota(
    val id: Long,
    val usuarioId: Long,
    val texto: String
)
