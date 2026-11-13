package br.edu.up.app.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

// Banco SQLite do app. Só cria as tabelas: os dados entram pelas telas, nunca por aqui.
// Notas, categorias e locais têm usuario_id: cada usuário só vê os próprios dados.
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
        // categoria_id = 0 quando a nota não tem categoria.
        db.execSQL(
            """
            CREATE TABLE notas(
                id INTEGER PRIMARY KEY,
                usuario_id INTEGER NOT NULL,
                texto TEXT NOT NULL,
                categoria_id INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        criarCategorias(db)
        criarLocais(db)
    }

    // Versão 1 tinha só usuarios e notas. A versão 2 cria categorias e locais
    // e adiciona a categoria nas notas, sem apagar o que já estava salvo.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE notas ADD COLUMN categoria_id INTEGER NOT NULL DEFAULT 0")
            criarCategorias(db)
            criarLocais(db)
        }
    }

    private fun criarCategorias(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE categorias(
                id INTEGER PRIMARY KEY,
                usuario_id INTEGER NOT NULL,
                nome TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    private fun criarLocais(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE locais(
                id INTEGER PRIMARY KEY,
                usuario_id INTEGER NOT NULL,
                nome TEXT NOT NULL,
                cep TEXT NOT NULL,
                endereco TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    companion object {
        private const val NOME_BANCO = "notas.db"
        private const val VERSAO = 2
    }
}
