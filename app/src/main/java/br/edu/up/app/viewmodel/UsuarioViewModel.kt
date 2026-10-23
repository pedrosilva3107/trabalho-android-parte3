package br.edu.up.app.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import br.edu.up.app.data.BancoHelper
import br.edu.up.app.data.UsuarioRepository
import br.edu.up.app.model.Usuario

// Login, cadastro, sessão e CRUD de usuários.
// As funções que podem falhar devolvem a mensagem de erro, ou null se deu certo.
class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = UsuarioRepository(BancoHelper(application))

    val usuarios = mutableStateListOf<Usuario>()

    // Sessão: enquanto for null, ninguém está logado.
    var usuarioLogado by mutableStateOf<Usuario?>(null)
        private set

    var usuarioSelecionado by mutableStateOf<Usuario?>(null)
        private set

    fun entrar(login: String, senha: String): String? {
        if (login.isBlank() || senha.isBlank()) {
            return "Preencha usuário e senha."
        }
        val usuario = repositorio.buscarPorLogin(login.trim())
        if (usuario == null || usuario.senha != senha) {
            return "Usuário ou senha inválidos."
        }
        usuarioLogado = usuario
        return null
    }

    fun sair() {
        usuarioLogado = null
        usuarioSelecionado = null
    }

    fun carregarUsuarios() {
        usuarios.clear()
        usuarios.addAll(repositorio.listar())
    }

    fun selecionar(usuario: Usuario) {
        usuarioSelecionado = usuario
    }

    fun cadastrar(nome: String, login: String, senha: String, confirmacao: String): String? {
        if (nome.isBlank() || login.isBlank() || senha.isBlank()) {
            return "Preencha todos os campos."
        }
        if (senha.length < 4) {
            return "A senha precisa ter pelo menos 4 caracteres."
        }
        if (senha != confirmacao) {
            return "As senhas não conferem."
        }
        if (repositorio.buscarPorLogin(login.trim()) != null) {
            return "Esse usuário já existe."
        }
        repositorio.inserir(Usuario(nome = nome.trim(), login = login.trim(), senha = senha))
        carregarUsuarios()
        return null
    }

    // Senha em branco mantém a senha atual.
    fun atualizar(usuario: Usuario, nome: String, login: String, novaSenha: String): String? {
        if (nome.isBlank() || login.isBlank()) {
            return "Preencha nome e usuário."
        }
        if (novaSenha.isNotEmpty() && novaSenha.length < 4) {
            return "A senha precisa ter pelo menos 4 caracteres."
        }
        val existente = repositorio.buscarPorLogin(login.trim())
        if (existente != null && existente.id != usuario.id) {
            return "Esse usuário já existe."
        }

        val atualizado = usuario.copy(
            nome = nome.trim(),
            login = login.trim(),
            senha = if (novaSenha.isEmpty()) usuario.senha else novaSenha
        )
        repositorio.atualizar(atualizado)

        if (usuarioLogado?.id == usuario.id) {
            usuarioLogado = atualizado
        }
        carregarUsuarios()
        return null
    }

    // Excluir a própria conta encerra a sessão.
    fun deletar(usuario: Usuario) {
        repositorio.deletar(usuario.id)
        usuarioSelecionado = null
        if (usuarioLogado?.id == usuario.id) {
            sair()
        }
        carregarUsuarios()
    }
}
