package br.edu.up.app.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

// Banco SQLite do app. Só cria as tabelas: os dados entram pelas telas
// (Cadastro e Criar nota), nunca por aqui.
internal class AppDatabase(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    File(context.noBackupFilesDir, NOME_BANCO).absolutePath,
    null,
    VERSAO
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios(
                id INTEGER PRIMARY KEY,
                nome TEXT NOT NULL,
                login TEXT NOT NULL UNIQUE,
                senha BLOB NOT NULL,
                salt BLOB NOT NULL
            )
            """.trimIndent()
        )
        // usuario_id guarda o dono da nota. Ao excluir um usuário,
        // o UsuarioDao.excluir apaga as notas dele antes.
        db.execSQL(
            """
            CREATE TABLE notas(
                id INTEGER PRIMARY KEY,
                usuario_id INTEGER NOT NULL,
                texto TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    // Obrigatório no SQLiteOpenHelper, mas o banco está na versão 1:
    // não existe versão anterior para migrar.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        private const val NOME_BANCO = "notas.db"
        private const val VERSAO = 1
    }
}
