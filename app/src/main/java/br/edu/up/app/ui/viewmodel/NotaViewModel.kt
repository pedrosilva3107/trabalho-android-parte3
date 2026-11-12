package br.edu.up.app.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.data.dal.NotaDao
import br.edu.up.app.data.model.Nota
import br.edu.up.app.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// CRUD das notas do usuário logado.
class NotaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = NotaDao(application)

    var notas by mutableStateOf<List<Nota>>(emptyList())
        private set
    var nota by mutableStateOf<Nota?>(null)
        private set
    var texto by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    // Roda a operação em segundo plano (Dispatchers.IO).
    // A operação recebe o id de quem está logado (sem sessão, não roda).
    private fun executar(op: (usuarioId: Long) -> Unit) {
        if (carregando || salvo) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                val usuarioId = SessaoUsuario.exigirUsuario()
                withContext(Dispatchers.IO) { op(usuarioId) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun listar() = executar { usuarioId ->
        notas = dao.listarPorUsuario(usuarioId)
    }

    fun buscarPorId(id: Long) {
        if (nota?.id == id) return
        executar { usuarioId ->
            val encontrada = dao.buscarPorId(id, usuarioId)
            require(encontrada != null) { "Nota não encontrada." }
            nota = encontrada
            texto = encontrada.texto
        }
    }

    fun mudarTexto(valor: String) {
        texto = valor
        mensagem = null
    }

    fun criar() = executar { usuarioId ->
        CadastroValidator.validarNota(texto)
        dao.inserir(usuarioId, texto.trim())
        salvo = true
    }

    fun salvar() = executar { usuarioId ->
        val selecionada = nota
        require(selecionada != null) { "Selecione uma nota." }
        CadastroValidator.validarNota(texto)
        dao.alterar(selecionada.id, usuarioId, texto.trim())
        salvo = true
    }

    fun excluir() = executar { usuarioId ->
        val selecionada = nota
        require(selecionada != null) { "Selecione uma nota." }
        dao.excluir(selecionada.id, usuarioId)
        salvo = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
