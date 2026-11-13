package br.edu.up.app.data.model

data class Local(
    val id: Long,
    val usuarioId: Long,
    val nome: String,
    val cep: String,
    val endereco: String
)
