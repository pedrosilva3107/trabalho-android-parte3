package br.edu.up.app.data.model

// Model: um local da tabela "locais" (ex.: Faculdade, Casa).
// O endereço é preenchido pela API ViaCEP a partir do CEP.
data class Local(
    val id: Long,
    val usuarioId: Long,
    val nome: String,
    val cep: String,
    val endereco: String
)
