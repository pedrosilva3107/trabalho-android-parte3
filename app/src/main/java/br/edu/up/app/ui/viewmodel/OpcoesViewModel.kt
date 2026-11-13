package br.edu.up.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OpcoesViewModel : ViewModel() {
    var modoEscuro by mutableStateOf(false)
        private set

    var tamanhoFonte by mutableStateOf(16)
        private set

    var corPostitIndex by mutableStateOf(0)
        private set

    fun escolherCor(indice: Int) {
        corPostitIndex = indice
    }

    fun diminuirFonte() {
        if (tamanhoFonte > 12) tamanhoFonte -= 2
    }

    fun aumentarFonte() {
        if (tamanhoFonte < 24) tamanhoFonte += 2
    }

    fun alternarModoEscuro() {
        modoEscuro = !modoEscuro
    }
}
