package br.edu.up.app.data.model

// Model: uma categoria da tabela "categorias" (ex.: Faculdade, Mercado).
data class Categoria(
    val id: Long,
    val usuarioId: Long,
    val nome: String
)
