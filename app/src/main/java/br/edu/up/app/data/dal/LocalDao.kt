package br.edu.up.app.data.dal

import android.content.ContentValues
import android.content.Context
import br.edu.up.app.data.local.AppDatabase
import br.edu.up.app.data.model.Local

// CRUD da tabela "locais". Toda consulta filtra pelo usuario_id.
internal class LocalDao(context: Context) {
    private val appContext = context.applicationContext

    fun inserir(usuarioId: Long, nome: String, cep: String, endereco: String): Long {
        return AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("nome", nome)
                put("cep", cep)
                put("endereco", endereco)
            }
            db.writableDatabase.insertOrThrow("locais", null, dados)
        }
    }

    fun listarPorUsuario(usuarioId: Long): List<Local> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "locais",
            arrayOf("id", "usuario_id", "nome", "cep", "endereco"),
            "usuario_id = ?",
            arrayOf(usuarioId.toString()),
            null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        Local(
                            cursor.getLong(0), cursor.getLong(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4)
                        )
                    )
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): Local? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "locais",
            arrayOf("id", "usuario_id", "nome", "cep", "endereco"),
            "id = ? AND usuario_id = ?",
            arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Local(
                    cursor.getLong(0), cursor.getLong(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)
                )
            } else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, nome: String, cep: String, endereco: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("cep", cep)
                put("endereco", endereco)
            }
            val alterados = db.writableDatabase.update(
                "locais", dados,
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterados == 1) { "Erro ao alterar o local." }
        }
    }

    fun excluir(id: Long, usuarioId: Long) {
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "locais",
                "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(excluidos == 1) { "O local não pôde ser excluído." }
        }
    }
}
