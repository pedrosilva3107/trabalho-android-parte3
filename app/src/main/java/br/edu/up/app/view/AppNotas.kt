package br.edu.up.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.up.app.viewmodel.NotaViewModel
import br.edu.up.app.viewmodel.OpcoesViewModel
import br.edu.up.app.viewmodel.UsuarioViewModel

enum class Tela {
    LOGIN,
    CADASTRO,
    LISTA,
    CRIAR,
    EDITAR,
    USUARIOS,
    NOVO_USUARIO,
    EDITAR_USUARIO,
    OPCOES
}

@Composable
fun AppNotas(
    usuarioViewModel: UsuarioViewModel = viewModel(),
    notaViewModel: NotaViewModel = viewModel(),
    opcoesViewModel: OpcoesViewModel = viewModel()
) {
    var telaAtual by remember { mutableStateOf(Tela.LOGIN) }
    var mensagemLogin by remember { mutableStateOf("") }

    val paleta = if (opcoesViewModel.modoEscuro) PaletaEscura else PaletaClara
    val corPostit = CORES_POSTIT[opcoesViewModel.corPostitIndex]
    val tamanhoFonte = opcoesViewModel.tamanhoFonte
    val usuarioLogado = usuarioViewModel.usuarioLogado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.fundo)
            .systemBarsPadding()
    ) {

        // Controle de sessão: sem usuário logado só existem login e cadastro,
        // e o menu não é desenhado.
        if (usuarioLogado == null) {

            if (telaAtual == Tela.CADASTRO) {
                TelaCadastro(
                    usuarioViewModel = usuarioViewModel,
                    paleta = paleta,
                    corPostit = corPostit,
                    titulo = "Criar conta",
                    onConcluido = {
                        mensagemLogin = "Conta criada. Faça login."
                        telaAtual = Tela.LOGIN
                    },
                    onVoltar = { telaAtual = Tela.LOGIN }
                )
            } else {
                TelaLogin(
                    usuarioViewModel = usuarioViewModel,
                    paleta = paleta,
                    corPostit = corPostit,
                    mensagem = mensagemLogin,
                    onEntrou = {
                        mensagemLogin = ""
                        telaAtual = Tela.LISTA
                    },
                    onCadastrar = {
                        mensagemLogin = ""
                        telaAtual = Tela.CADASTRO
                    }
                )
            }

        } else {

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (telaAtual) {

                    Tela.LISTA -> TelaListaNotas(
                        notaViewModel = notaViewModel,
                        usuario = usuarioLogado,
                        paleta = paleta,
                        corPostit = corPostit,
                        tamanhoFonte = tamanhoFonte,
                        onCriar = { telaAtual = Tela.CRIAR },
                        onAbrir = { nota ->
                            notaViewModel.selecionar(nota)
                            telaAtual = Tela.EDITAR
                        }
                    )

                    Tela.CRIAR -> TelaCriarNota(
                        notaViewModel = notaViewModel,
                        usuarioId = usuarioLogado.id,
                        paleta = paleta,
                        corPostit = corPostit,
                        tamanhoFonte = tamanhoFonte,
                        onVoltar = { telaAtual = Tela.LISTA }
                    )

                    Tela.EDITAR -> TelaEditarNota(
                        notaViewModel = notaViewModel,
                        paleta = paleta,
                        corPostit = corPostit,
                        tamanhoFonte = tamanhoFonte,
                        onVoltar = { telaAtual = Tela.LISTA }
                    )

                    Tela.USUARIOS -> TelaUsuarios(
                        usuarioViewModel = usuarioViewModel,
                        paleta = paleta,
                        corPostit = corPostit,
                        onNovo = { telaAtual = Tela.NOVO_USUARIO },
                        onAbrir = { usuario ->
                            usuarioViewModel.selecionar(usuario)
                            telaAtual = Tela.EDITAR_USUARIO
                        }
                    )

                    Tela.NOVO_USUARIO -> TelaCadastro(
                        usuarioViewModel = usuarioViewModel,
                        paleta = paleta,
                        corPostit = corPostit,
                        titulo = "Novo usuário",
                        onConcluido = { telaAtual = Tela.USUARIOS },
                        onVoltar = { telaAtual = Tela.USUARIOS }
                    )

                    Tela.EDITAR_USUARIO -> TelaEditarUsuario(
                        usuarioViewModel = usuarioViewModel,
                        paleta = paleta,
                        corPostit = corPostit,
                        onVoltar = { telaAtual = Tela.USUARIOS },
                        onExcluiu = { excluiuPropriaConta ->
                            if (excluiuPropriaConta) {
                                notaViewModel.limpar()
                                mensagemLogin = "Conta excluída."
                                telaAtual = Tela.LOGIN
                            } else {
                                telaAtual = Tela.USUARIOS
                            }
                        }
                    )

                    Tela.OPCOES -> TelaOpcoes(
                        opcoesViewModel = opcoesViewModel,
                        paleta = paleta,
                        corPostit = corPostit
                    )

                    // Com sessão ativa estas telas não são usadas.
                    Tela.LOGIN, Tela.CADASTRO -> {}
                }
            }

            MenuInferior(
                telaAtual = telaAtual,
                paleta = paleta,
                onNotas = { telaAtual = Tela.LISTA },
                onUsuarios = { telaAtual = Tela.USUARIOS },
                onOpcoes = { telaAtual = Tela.OPCOES },
                onSair = {
                    usuarioViewModel.sair()
                    notaViewModel.limpar()
                    telaAtual = Tela.LOGIN
                }
            )
        }
    }
}
