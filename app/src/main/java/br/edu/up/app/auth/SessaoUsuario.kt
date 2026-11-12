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

    val usuarioId: Long? get() = usuario?.id

    fun iniciar(usuario: Usuario) {
        require(usuario.id > 0)
        this.usuario = usuario
    }

    // Chamado quando o próprio usuário edita nome/login.
    fun atualizar(usuario: Usuario) {
        if (usuario.id == usuarioId) this.usuario = usuario
    }

    fun encerrar() {
        usuario = null
    }

    fun exigirUsuario(): Long = requireNotNull(usuarioId) {
        "Faça login para continuar."
    }
}
