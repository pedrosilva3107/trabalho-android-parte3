package br.edu.up.app.data.model

data class Nota(
    val id: Long,
    val usuarioId: Long,
    val texto: String,
    val categoriaId: Long
)
