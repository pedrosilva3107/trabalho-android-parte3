package br.edu.up.app.data

import android.content.ContentValues
import android.database.Cursor
import br.edu.up.app.model.Usuario

// CRUD da tabela "usuarios" e consulta de login.
class UsuarioRepository(private val banco: BancoHelper) {

    fun inserir(usuario: Usuario): Long {
        val db = banco.writableDatabase
        val valores = ContentValues()
        valores.put("nome", usuario.nome)
        valores.put("login", usuario.login)
        valores.put("senha", usuario.senha)
        return db.insert("usuarios", null, valores)
    }

    fun listar(): List<Usuario> {
        val db = banco.readableDatabase
        val cursor = db.rawQuery("SELECT id, nome, login, senha FROM usuarios ORDER BY nome", null)
        val usuarios = mutableListOf<Usuario>()
        while (cursor.moveToNext()) {
            usuarios.add(lerUsuario(cursor))
        }
        cursor.close()
        return usuarios
    }

    fun buscarPorLogin(login: String): Usuario? {
        val db = banco.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nome, login, senha FROM usuarios WHERE login = ?",
            arrayOf(login)
        )
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = lerUsuario(cursor)
        }
        cursor.close()
        return usuario
    }

    fun atualizar(usuario: Usuario): Int {
        val db = banco.writableDatabase
        val valores = ContentValues()
        valores.put("nome", usuario.nome)
        valores.put("login", usuario.login)
        valores.put("senha", usuario.senha)
        return db.update("usuarios", valores, "id = ?", arrayOf(usuario.id.toString()))
    }

    // Apaga o usuário e as notas dele.
    fun deletar(id: Int): Int {
        val db = banco.writableDatabase
        db.delete("notas", "usuario_id = ?", arrayOf(id.toString()))
        return db.delete("usuarios", "id = ?", arrayOf(id.toString()))
    }

    private fun lerUsuario(cursor: Cursor): Usuario {
        return Usuario(
            id = cursor.getInt(0),
            nome = cursor.getString(1),
            login = cursor.getString(2),
            senha = cursor.getString(3)
        )
    }
}
