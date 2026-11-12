package br.edu.up.app.validation

// Regras dos formulários. Cada require lança IllegalArgumentException
// com a mensagem que aparece na tela.
object CadastroValidator {

    fun validarCadastro(nome: String, login: String, senha: String, confirmacao: String) {
        require(nome.isNotBlank() && login.isNotBlank() && senha.isNotBlank()) {
            "Preencha todos os campos."
        }
        validarSenha(senha)
        require(senha == confirmacao) { "As senhas não conferem." }
    }

    // Senha vazia = manter a atual.
    fun validarAlteracao(nome: String, login: String, novaSenha: String) {
        require(nome.isNotBlank() && login.isNotBlank()) { "Preencha nome e usuário." }
        if (novaSenha.isNotEmpty()) validarSenha(novaSenha)
    }

    fun validarNota(texto: String) {
        require(texto.isNotBlank()) { "Escreva algo antes de salvar." }
    }

    private fun validarSenha(senha: String) {
        require(senha.length >= 4) { "A senha precisa ter pelo menos 4 caracteres." }
    }
}
