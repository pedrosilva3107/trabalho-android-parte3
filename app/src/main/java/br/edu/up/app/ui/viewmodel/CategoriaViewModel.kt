package br.edu.up.app.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.data.dal.CategoriaDao
import br.edu.up.app.data.model.Categoria
import br.edu.up.app.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// CRUD das categorias do usuário logado.
class CategoriaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CategoriaDao(application)

    var categorias by mutableStateOf<List<Categoria>>(emptyList())
        private set
    // null = categoria nova (tela em modo "criar")
    var categoria by mutableStateOf<Categoria?>(null)
        private set
    var nome by mutableStateOf("")
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
        categorias = dao.listarPorUsuario(usuarioId)
    }

    fun buscarPorId(id: Long) {
        if (categoria?.id == id) return
        executar { usuarioId ->
            val encontrada = dao.buscarPorId(id, usuarioId)
            require(encontrada != null) { "Categoria não encontrada." }
            categoria = encontrada
            nome = encontrada.nome
        }
    }

    fun mudarNome(valor: String) {
        nome = valor
        mensagem = null
    }

    // Sem categoria selecionada cria uma nova; com categoria selecionada altera.
    fun salvar() = executar { usuarioId ->
        CadastroValidator.validarCategoria(nome)
        val selecionada = categoria
        if (selecionada == null) {
            dao.inserir(usuarioId, nome.trim())
        } else {
            dao.alterar(selecionada.id, usuarioId, nome.trim())
        }
        salvo = true
    }

    fun excluir() = executar { usuarioId ->
        val selecionada = categoria
        require(selecionada != null) { "Selecione uma categoria." }
        dao.excluir(selecionada.id, usuarioId)
        salvo = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
