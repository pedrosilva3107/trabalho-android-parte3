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
import br.edu.up.app.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Login e cadastro (telas públicas, sem sessão).
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)

    var carregando by mutableStateOf(false)
        private set
    var concluido by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    // Roda a operação em segundo plano e transforma os erros em mensagem.
    private fun executar(op: suspend () -> Boolean) {
        if (carregando || concluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                concluido = op()
                if (!concluido) mensagem = "Usuário ou senha inválidos."
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

    fun cadastrar(nome: String, login: String, senha: String, confirmacao: String) = executar {
        val nomeLimpo = nome.trim()
        val loginLimpo = login.trim()
        CadastroValidator.validarCadastro(nomeLimpo, loginLimpo, senha, confirmacao)

        withContext(Dispatchers.IO) {
            val salt = AuthManager.criarSalt()
            val senhaHash = AuthManager.gerarHash(senha, salt)
            try {
                dao.cadastrar(nomeLimpo, loginLimpo, senhaHash, salt)
            } finally {
                senhaHash.fill(0)
            }
        }
        true
    }

    fun entrar(login: String, senha: String) = executar {
        require(login.isNotBlank() && senha.isNotBlank()) { "Preencha usuário e senha." }

        val usuario = withContext(Dispatchers.IO) {
            val dados = dao.buscarSenha(login.trim())
            // Mesmo sem usuário, confere um hash "vazio" para o tempo de resposta ser igual.
            val senhaHash = dados?.hash ?: ByteArray(32)
            val salt = dados?.salt ?: ByteArray(16)
            val autenticado = try {
                AuthManager.validarSenha(senha, salt, senhaHash)
            } finally {
                senhaHash.fill(0)
            }
            if (autenticado && dados != null) dao.buscarPorId(dados.usuarioId) else null
        }

        if (usuario != null) {
            SessaoUsuario.iniciar(usuario)
            true
        } else false
    }

    fun limparMensagem() {
        mensagem = null
    }

    fun consumirResultado() {
        concluido = false
    }
}
