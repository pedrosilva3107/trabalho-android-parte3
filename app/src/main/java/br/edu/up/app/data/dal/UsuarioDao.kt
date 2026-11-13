package br.edu.up.app.data.dal

import android.content.ContentValues
import android.content.Context
import br.edu.up.app.data.local.AppDatabase
import br.edu.up.app.data.model.Usuario

// Hash e salt da senha de um usuário (usado só no login).
internal class SenhaSalva(val hash: ByteArray, val salt: ByteArray, val usuarioId: Long)

// CRUD da tabela "usuarios".
internal class UsuarioDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, login: String, senha: ByteArray, salt: ByteArray): Long {
        return AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("login", login)
                put("senha", senha)
                put("salt", salt)
            }
            db.writableDatabase.insertOrThrow("usuarios", null, dados)
        }
    }

    fun buscarSenha(login: String): SenhaSalva? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios",
            arrayOf("senha", "salt", "id"),
            "login = ?",
            arrayOf(login),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                SenhaSalva(cursor.getBlob(0), cursor.getBlob(1), cursor.getLong(2))
            } else null
        }
    }

    fun listar(): List<Usuario> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios",
            arrayOf("id", "nome", "login"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Usuario? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios",
            arrayOf("id", "nome", "login"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2))
            } else null
        }
    }

    // hash e salt nulos = mantém a senha atual.
    fun alterar(
        id: Long,
        nome: String,
        login: String,
        hash: ByteArray? = null,
        salt: ByteArray? = null
    ) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("login", login)
                if (hash != null) put("senha", hash)
                if (salt != null) put("salt", salt)
            }
            val alterados = db.writableDatabase.update(
                "usuarios", dados, "id = ?", arrayOf(id.toString())
            )
            require(alterados == 1) { "Erro ao alterar o usuário." }
        }
    }

    // Apaga primeiro as notas, categorias e locais do usuário e depois o próprio usuário.
    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            db.writableDatabase.delete(
                "notas", "usuario_id = ?", arrayOf(id.toString())
            )
            db.writableDatabase.delete(
                "categorias", "usuario_id = ?", arrayOf(id.toString())
            )
            db.writableDatabase.delete(
                "locais", "usuario_id = ?", arrayOf(id.toString())
            )
            val excluidos = db.writableDatabase.delete(
                "usuarios", "id = ?", arrayOf(id.toString())
            )
            require(excluidos == 1) { "O usuário não pôde ser excluído." }
        }
    }
}
