package br.edu.up.app.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.up.app.auth.SessaoUsuario
import br.edu.up.app.data.Endereco
import br.edu.up.app.data.ViaCepApi
import br.edu.up.app.data.dal.LocalDao
import br.edu.up.app.data.model.Local
import br.edu.up.app.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = LocalDao(application)

    var locais by mutableStateOf<List<Local>>(emptyList())
        private set
    var local by mutableStateOf<Local?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var cep by mutableStateOf("")
        private set
    var endereco by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

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
        locais = dao.listarPorUsuario(usuarioId)
    }

    fun buscarPorId(id: Long) {
        if (local?.id == id) return
        executar { usuarioId ->
            val encontrado = dao.buscarPorId(id, usuarioId)
            require(encontrado != null) { "Local não encontrado." }
            local = encontrado
            nome = encontrado.nome
            cep = encontrado.cep
            endereco = encontrado.endereco
        }
    }

    fun mudarNome(valor: String) {
        nome = valor
        mensagem = null
    }

    fun mudarCep(valor: String) {
        if (valor.length > 9) return
        cep = valor
        mensagem = null
    }

    fun mudarEndereco(valor: String) {
        endereco = valor
        mensagem = null
    }

    fun buscarCep() {
        if (carregando) return
        mensagem = null

        if (!CadastroValidator.cepValido(cep.trim())) {
            mensagem = "Informe um CEP válido (ex.: 80010-000)."
            return
        }
        val cepConsulta = cep.trim().replace("-", "")
        carregando = true
        viewModelScope.launch {
            try {
                val resposta = ViaCepApi.service.buscar(cepConsulta)
                when {
                    resposta.erro == true -> mensagem = "CEP não encontrado."
                    resposta.cep.isNullOrBlank() -> mensagem = "Resposta inválida."
                    else -> endereco = montarEndereco(resposta)
                }
            } catch (erro: CancellationException) {
                throw erro
            } catch (_: Exception) {
                mensagem = "Não foi possível consultar o CEP. Verifique a internet."
            } finally {
                carregando = false
            }
        }
    }

    private fun montarEndereco(resposta: Endereco): String {
        var texto = ""
        if (!resposta.rua.isNullOrBlank()) texto += resposta.rua + ", "
        if (!resposta.bairro.isNullOrBlank()) texto += resposta.bairro + ", "
        texto += resposta.cidade.orEmpty() + " - " + resposta.uf.orEmpty()
        return texto
    }

    fun salvar() = executar { usuarioId ->
        val nomeLimpo = nome.trim()
        val cepLimpo = cep.trim()
        val enderecoLimpo = endereco.trim()
        CadastroValidator.validarLocal(nomeLimpo, cepLimpo, enderecoLimpo)

        val selecionado = local
        if (selecionado == null) {
            dao.inserir(usuarioId, nomeLimpo, cepLimpo, enderecoLimpo)
        } else {
            dao.alterar(selecionado.id, usuarioId, nomeLimpo, cepLimpo, enderecoLimpo)
        }
        salvo = true
    }

    fun excluir() = executar { usuarioId ->
        val selecionado = local
        require(selecionado != null) { "Selecione um local." }
        dao.excluir(selecionado.id, usuarioId)
        salvo = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
