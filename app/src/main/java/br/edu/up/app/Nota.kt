package br.edu.up.app

data class Nota(
    val id: Int,
    val texto: String
)

enum class Tela {
    LOGIN,
    LISTA,
    CRIAR,
    EDITAR,
    OPCOES
}
