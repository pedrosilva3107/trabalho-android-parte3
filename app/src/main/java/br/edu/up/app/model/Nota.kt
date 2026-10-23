package br.edu.up.app.model

// Model: representa uma linha da tabela "notas".
data class Nota(
    val id: Int = 0,
    val usuarioId: Int,
    val texto: String
)
