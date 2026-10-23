package br.edu.up.app.data

import android.content.ContentValues
import br.edu.up.app.model.Nota

// CRUD da tabela "notas".
class NotaRepository(private val banco: BancoHelper) {

    fun inserir(nota: Nota): Long {
        val db = banco.writableDatabase
        val valores = ContentValues()
        valores.put("usuario_id", nota.usuarioId)
        valores.put("texto", nota.texto)
        return db.insert("notas", null, valores)
    }

    fun listarPorUsuario(usuarioId: Int): List<Nota> {
        val db = banco.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, usuario_id, texto FROM notas WHERE usuario_id = ? ORDER BY id",
            arrayOf(usuarioId.toString())
        )
        val notas = mutableListOf<Nota>()
        while (cursor.moveToNext()) {
            notas.add(
                Nota(
                    id = cursor.getInt(0),
                    usuarioId = cursor.getInt(1),
                    texto = cursor.getString(2)
                )
            )
        }
        cursor.close()
        return notas
    }

    fun atualizar(nota: Nota): Int {
        val db = banco.writableDatabase
        val valores = ContentValues()
        valores.put("texto", nota.texto)
        return db.update("notas", valores, "id = ?", arrayOf(nota.id.toString()))
    }

    fun deletar(id: Int): Int {
        val db = banco.writableDatabase
        return db.delete("notas", "id = ?", arrayOf(id.toString()))
    }
}
