package br.edu.up.app.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.edu.up.app.data.model.Usuario

internal object SessaoUsuario {
    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun iniciar(usuario: Usuario) {
        require(usuario.id > 0)
        this.usuario = usuario
    }

    fun atualizar(usuario: Usuario) {
        if (usuario.id == this.usuario?.id) this.usuario = usuario
    }

    fun encerrar() {
        usuario = null
    }

    fun exigirUsuario(): Long {
        val atual = usuario
        require(atual != null) { "Faça login para continuar." }
        return atual.id
    }
}
