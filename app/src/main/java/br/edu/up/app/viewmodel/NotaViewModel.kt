package br.edu.up.app.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import br.edu.up.app.data.BancoHelper
import br.edu.up.app.data.NotaRepository
import br.edu.up.app.model.Nota

// CRUD de notas do usuário logado.
// As funções que podem falhar devolvem a mensagem de erro, ou null se deu certo.
class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NotaRepository(BancoHelper(application))

    val notas = mutableStateListOf<Nota>()

    var notaSelecionada by mutableStateOf<Nota?>(null)
        private set

    fun carregarNotas(usuarioId: Int) {
        notas.clear()
        notas.addAll(repositorio.listarPorUsuario(usuarioId))
    }

    // Usado no logout, para a lista não mostrar notas de outro usuário.
    fun limpar() {
        notas.clear()
        notaSelecionada = null
    }

    fun selecionar(nota: Nota) {
        notaSelecionada = nota
    }

    fun criar(usuarioId: Int, texto: String): String? {
        if (texto.isBlank()) {
            return "Escreva algo antes de salvar."
        }
        repositorio.inserir(Nota(usuarioId = usuarioId, texto = texto.trim()))
        carregarNotas(usuarioId)
        return null
    }

    fun atualizar(nota: Nota, novoTexto: String): String? {
        if (novoTexto.isBlank()) {
            return "Escreva algo antes de salvar."
        }
        repositorio.atualizar(nota.copy(texto = novoTexto.trim()))
        carregarNotas(nota.usuarioId)
        return null
    }

    fun excluir(nota: Nota) {
        repositorio.deletar(nota.id)
        notaSelecionada = null
        carregarNotas(nota.usuarioId)
    }
}
