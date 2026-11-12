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

// CRUD de usuários (só com sessão ativa).
class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)

    val usuarioLogadoId: Long? get() = SessaoUsuario.usuarioId

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

    private fun executar(op: suspend () -> Unit) {
        if (carregando || salvo || excluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                op()
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
        usuarios = withContext(Dispatchers.IO) { dao.listar() }
    }

    fun buscarPorId(id: Long) {
        if (usuario?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            usuario = requireNotNull(encontrado) { "Usuário não encontrado." }
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

    // Senha em branco mantém a senha atual.
    fun salvar(novaSenha: String) = executar {
        val selecionado = requireNotNull(usuario) { "Selecione um usuário." }
        val nomeLimpo = nome.trim()
        val loginLimpo = login.trim()
        CadastroValidator.validarAlteracao(nomeLimpo, loginLimpo, novaSenha)

        withContext(Dispatchers.IO) {
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
        }

        val atualizado = selecionado.copy(nome = nomeLimpo, login = loginLimpo)
        usuario = atualizado
        SessaoUsuario.atualizar(atualizado) // se for você, a sessão já mostra o nome novo
        salvo = true
    }

    // Quem decide o que fazer depois (voltar ou encerrar a sessão) é a navegação.
    fun excluir() = executar {
        val selecionado = requireNotNull(usuario) { "Selecione um usuário." }
        withContext(Dispatchers.IO) { dao.excluir(selecionado.id) }
        excluiuPropriaConta = selecionado.id == SessaoUsuario.usuarioId
        excluido = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
