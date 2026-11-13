package br.edu.up.app.data

import com.google.gson.annotations.SerializedName

// Resposta da API ViaCEP (https://viacep.com.br/ws/{cep}/json/).
data class Endereco(
    @SerializedName("cep") val cep: String? = null,
    @SerializedName("logradouro") val rua: String? = null,
    @SerializedName("complemento") val complemento: String? = null,
    @SerializedName("bairro") val bairro: String? = null,
    @SerializedName("localidade") val cidade: String? = null,
    @SerializedName("uf") val uf: String? = null,
    @SerializedName("erro") val erro: Boolean? = null,
)
