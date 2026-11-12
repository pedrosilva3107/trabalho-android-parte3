package br.edu.up.app.navigation

import kotlinx.serialization.Serializable

// Rotas do app. O @Serializable deixa a navegação tipada:
// os parâmetros (como o id) viajam junto com a rota.

// Telas públicas (sem login)
@Serializable
data class Login(val mensagem: String = "")

@Serializable
data object Cadastro

// Telas que exigem usuário logado
@Serializable
data object ListaNotas

@Serializable
data object CriarNota

@Serializable
data class EditarNota(val id: Long)

@Serializable
data object Usuarios

@Serializable
data object NovoUsuario

@Serializable
data class EditarUsuario(val id: Long)

@Serializable
data object Opcoes
