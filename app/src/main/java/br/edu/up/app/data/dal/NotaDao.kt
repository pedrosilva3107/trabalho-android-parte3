package br.edu.up.app.data.dal

import android.content.ContentValues
import android.content.Context
import br.edu.up.app.data.local.AppDatabase
import br.edu.up.app.data.model.Nota

// CRUD da tabela "notas". Toda consulta filtra pelo usuario_id,
// então um usuário nunca lê nem altera a nota de outro.
internal class NotaDao(context: Context) {
    private val appContext = context.applicationContext

    fun inserir(usuarioId: Long, texto: String): Long {
        return AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("texto", texto)
            }
            db.writableDatabase.insertOrThrow("notas", null, dados)
        }
    }

    fun listarPorUsuario(usuarioId: Long): List<Nota> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "notas",
            arrayOf("id", "usuario_id", "texto"),
            "usuario_id = ?",
            arrayOf(usuarioId.toString()),
            null, null,
            "id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Nota(cursor.getLong(0), cursor.getLong(1), cursor.getString(2)))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): Nota? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "notas",
            arrayOf("id", "usuario_id", "texto"),
            "id = ? AND usuario_id = ?",
            arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Nota(cursor.getLong(0), cursor.getLong(1), cursor.getString(2))
            } else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, texto: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply { put("texto", texto) }
            val alterados = db.writableDatabase.update(
                "notas", dados,
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterados == 1) { "Erro ao alterar a nota." }
        }
    }

    fun excluir(id: Long, usuarioId: Long) {
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "notas",
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(excluidos == 1) { "A nota não pôde ser excluída." }
        }
    }
}
