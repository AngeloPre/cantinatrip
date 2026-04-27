package com.example.cantinatrip.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        const val DATABASE_NAME = "cantina_trip.db"
        const val DATABASE_VERSION = 1
        const val TABLE_VENDAS = "vendas"
        const val TABLE_ITENS = "itens_venda"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createVendas = """
            CREATE TABLE $TABLE_VENDAS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome_comprador TEXT NOT NULL,
                dataHora TEXT NOT NULL,
                valorTotal REAL NOT NULL
            )
        """.trimIndent()

        val createItens = """
            CREATE TABLE $TABLE_ITENS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                vendaId INTEGER NOT NULL,
                produtoId INTEGER NOT NULL,
                quantidade INTEGER NOT NULL,
                subtotal REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(createVendas)
        db.execSQL(createItens)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITENS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VENDAS")
        onCreate(db)
    }

}