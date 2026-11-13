package br.edu.up.app.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.up.app.auth.AuthManager
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.data.dal.UsuarioDao
import br.edu.up.app.data.model.Usuario
import br.edu.up.app.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)

    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var login by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var excluido by mutableStateOf(false)
        private set
    var excluiuPropriaConta by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    fun usuarioLogadoId(): Long? = SessaoUsuario.usuario?.id

    private fun executar(op: () -> Unit) {
        if (carregando || salvo || excluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                withContext(Dispatchers.IO) { op() }
            } catch (e: CancellationException) {
                throw e
            } catch (e: SQLiteConstraintException) {
                mensagem = "Esse usuário já existe."
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun listar() = executar {
        usuarios = dao.listar()
    }

    fun buscarPorId(id: Long) {
        if (usuario?.id == id) return
        executar {
            val encontrado = dao.buscarPorId(id)
            require(encontrado != null) { "Usuário não encontrado." }
            usuario = encontrado
            nome = encontrado.nome
            login = encontrado.login
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando) nome = valor
    }

    fun mudarLogin(valor: String) {
        if (!carregando) login = valor
    }

    fun salvar(novaSenha: String) = executar {
        val selecionado = usuario
        require(selecionado != null) { "Selecione um usuário." }
        val nomeLimpo = nome.trim()
        val loginLimpo = login.trim()
        CadastroValidator.validarAlteracao(nomeLimpo, loginLimpo, novaSenha)

        if (novaSenha.isEmpty()) {
            dao.alterar(selecionado.id, nomeLimpo, loginLimpo)
        } else {
            val salt = AuthManager.criarSalt()
            val hash = AuthManager.gerarHash(novaSenha, salt)
            try {
                dao.alterar(selecionado.id, nomeLimpo, loginLimpo, hash, salt)
            } finally {
                hash.fill(0)
            }
        }

        val atualizado = selecionado.copy(nome = nomeLimpo, login = loginLimpo)
        usuario = atualizado
        SessaoUsuario.atualizar(atualizado)
        salvo = true
    }

    fun excluir() = executar {
        val selecionado = usuario
        require(selecionado != null) { "Selecione um usuário." }
        dao.excluir(selecionado.id)
        excluiuPropriaConta = selecionado.id == SessaoUsuario.usuario?.id
        excluido = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
