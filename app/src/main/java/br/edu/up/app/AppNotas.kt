package br.edu.up.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppNotas() {

    var telaAtual by remember { mutableStateOf(Tela.LOGIN) }
    var notaSelecionada by remember { mutableStateOf<Nota?>(null) }

    val notas = remember {
        mutableStateListOf(
            Nota(1, "Comprar pão, café e frutas na feira"),
            Nota(2, "Estudar Kotlin para a prova de quinta"),
            Nota(3, "Reunião do grupo sábado às 14h"),
            Nota(4, "Ideia: app de lista de tarefas")
        )
    }
    var proximoId by remember { mutableStateOf(5) }
    var usuario by remember { mutableStateOf("") }

    var modoEscuro by remember { mutableStateOf(false) }
    var tamanhoFonte by remember { mutableStateOf(16) }
    var corPostitIndex by remember { mutableStateOf(0) }

    val paleta = if (modoEscuro) PaletaEscura else PaletaClara
    val corPostit = CORES_POSTIT[corPostitIndex]

    when (telaAtual) {

        Tela.LOGIN -> TelaLogin(
            paleta = paleta,
            corPostit = corPostit,
            onEntrar = { nome ->
                usuario = nome
                telaAtual = Tela.LISTA
            }
        )

        Tela.LISTA -> TelaListaNotas(
            notas = notas,
            paleta = paleta,
            corPostit = corPostit,
            tamanhoFonte = tamanhoFonte,
            onCriar = { telaAtual = Tela.CRIAR },
            onAbrir = { nota ->
                notaSelecionada = nota
                telaAtual = Tela.EDITAR
            },
            onOpcoes = { telaAtual = Tela.OPCOES }
        )

        Tela.CRIAR -> TelaCriarNota(
            paleta = paleta,
            corPostit = corPostit,
            tamanhoFonte = tamanhoFonte,
            onSalvar = { texto ->
                notas.add(Nota(proximoId, texto))
                proximoId += 1
                telaAtual = Tela.LISTA
            },
            onVoltar = { telaAtual = Tela.LISTA }
        )

        Tela.EDITAR -> {
            notaSelecionada?.let { nota ->
                TelaEditarNota(
                    nota = nota,
                    paleta = paleta,
                    corPostit = corPostit,
                    tamanhoFonte = tamanhoFonte,
                    onSalvar = { novoTexto ->
                        val posicao = notas.indexOf(nota)
                        if (posicao >= 0) {
                            notas[posicao] = nota.copy(texto = novoTexto)
                        }
                        telaAtual = Tela.LISTA
                    },
                    onExcluir = {
                        notas.remove(nota)
                        notaSelecionada = null
                        telaAtual = Tela.LISTA
                    },
                    onVoltar = { telaAtual = Tela.LISTA }
                )
            }
        }

        Tela.OPCOES -> TelaOpcoes(
            paleta = paleta,
            corPostit = corPostit,
            corPostitIndex = corPostitIndex,
            tamanhoFonte = tamanhoFonte,
            modoEscuro = modoEscuro,
            onCorPostit = { corPostitIndex = it },
            onDiminuirFonte = { if (tamanhoFonte > 12) tamanhoFonte -= 2 },
            onAumentarFonte = { if (tamanhoFonte < 24) tamanhoFonte += 2 },
            onAlternarModoEscuro = { modoEscuro = !modoEscuro },
            onVoltar = { telaAtual = Tela.LISTA }
        )
    }
}
