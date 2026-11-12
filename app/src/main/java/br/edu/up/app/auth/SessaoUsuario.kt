package br.edu.up.app.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.edu.up.app.data.model.Usuario

// Sessão: guarda quem está logado. null = ninguém logado.
// É um mutableStateOf, então as telas e a navegação reagem quando muda.
internal object SessaoUsuario {
    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun iniciar(usuario: Usuario) {
        require(usuario.id > 0)
        this.usuario = usuario
    }

    // Chamado quando o próprio usuário edita nome/login.
    fun atualizar(usuario: Usuario) {
        if (usuario.id == this.usuario?.id) this.usuario = usuario
    }

    fun encerrar() {
        usuario = null
    }

    // Devolve o id de quem está logado. Sem sessão, o require lança o erro
    // que vira mensagem na tela.
    fun exigirUsuario(): Long {
        val atual = usuario
        require(atual != null) { "Faça login para continuar." }
        return atual.id
    }
}
