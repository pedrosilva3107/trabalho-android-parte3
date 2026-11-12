package br.edu.up.app.data.model

// Model: um usuário da tabela "usuarios".
// A senha não fica aqui: ela só é lida no login (ver SenhaSalva no UsuarioDao).
data class Usuario(
    val id: Long,
    val nome: String,
    val login: String
)
