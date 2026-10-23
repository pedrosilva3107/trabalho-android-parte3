package br.edu.up.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Cria e atualiza o banco SQLite do app.
class BancoHelper(context: Context) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "login TEXT NOT NULL UNIQUE, " +
                "senha TEXT NOT NULL)"
        )
        db.execSQL(
            "CREATE TABLE notas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER NOT NULL, " +
                "texto TEXT NOT NULL)"
        )
        inserirDadosIniciais(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, versaoAntiga: Int, versaoNova: Int) {
        db.execSQL("DROP TABLE IF EXISTS notas")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // Usuário de demonstração (admin / 1234) com algumas notas.
    private fun inserirDadosIniciais(db: SQLiteDatabase) {
        val usuario = ContentValues()
        usuario.put("nome", "Administrador")
        usuario.put("login", "admin")
        usuario.put("senha", "1234")
        val usuarioId = db.insert("usuarios", null, usuario)

        val textos = listOf(
            "Comprar pão, café e frutas na feira",
            "Estudar Kotlin para a prova de quinta",
            "Reunião do grupo sábado às 14h",
            "Ideia: app de lista de tarefas"
        )
        for (texto in textos) {
            val nota = ContentValues()
            nota.put("usuario_id", usuarioId)
            nota.put("texto", texto)
            db.insert("notas", null, nota)
        }
    }

    companion object {
        const val NOME_BANCO = "notas.db"
        const val VERSAO = 1
    }
}
