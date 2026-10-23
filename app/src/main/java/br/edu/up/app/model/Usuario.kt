package br.edu.up.app.model

// Model: representa uma linha da tabela "usuarios".
data class Usuario(
    val id: Int = 0,
    val nome: String,
    val login: String,
    val senha: String
)
