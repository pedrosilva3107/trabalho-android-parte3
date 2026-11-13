package br.edu.up.app.data.dal

import android.content.ContentValues
import android.content.Context
import br.edu.up.app.data.local.AppDatabase
import br.edu.up.app.data.model.Categoria

// CRUD da tabela "categorias". Toda consulta filtra pelo usuario_id.
internal class CategoriaDao(context: Context) {
    private val appContext = context.applicationContext

    fun inserir(usuarioId: Long, nome: String): Long {
        return AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("nome", nome)
            }
            db.writableDatabase.insertOrThrow("categorias", null, dados)
        }
    }

    fun listarPorUsuario(usuarioId: Long): List<Categoria> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "categorias",
            arrayOf("id", "usuario_id", "nome"),
            "usuario_id = ?",
            arrayOf(usuarioId.toString()),
            null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Categoria(cursor.getLong(0), cursor.getLong(1), cursor.getString(2)))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): Categoria? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "categorias",
            arrayOf("id", "usuario_id", "nome"),
            "id = ? AND usuario_id = ?",
            arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Categoria(cursor.getLong(0), cursor.getLong(1), cursor.getString(2))
            } else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, nome: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply { put("nome", nome) }
            val alterados = db.writableDatabase.update(
                "categorias", dados,
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterados == 1) { "Erro ao alterar a categoria." }
        }
    }

    // As notas dessa categoria não são apagadas: só ficam "sem categoria" (categoria_id = 0).
    fun excluir(id: Long, usuarioId: Long) {
        AppDatabase(appContext).use { db ->
            val semCategoria = ContentValues().apply { put("categoria_id", 0L) }
            db.writableDatabase.update(
                "notas", semCategoria,
                "categoria_id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            val excluidos = db.writableDatabase.delete(
                "categorias",
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(excluidos == 1) { "A categoria não pôde ser excluída." }
        }
    }
}
