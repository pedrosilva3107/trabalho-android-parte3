package br.edu.up.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Login(val mensagem: String = "")

@Serializable
data object Cadastro

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
data object Categorias

@Serializable
data class CategoriaForm(val id: Long = 0L)

@Serializable
data object Locais

@Serializable
data class LocalForm(val id: Long = 0L)

@Serializable
data object Opcoes
