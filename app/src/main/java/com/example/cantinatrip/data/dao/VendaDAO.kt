package com.example.cantinatrip.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.cantinatrip.data.db.DBHelper
import com.example.cantinatrip.model.Venda

class VendaDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun addVenda(venda: Venda): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nome_comprador", venda.nomeComprador)
            put("dataHora", venda.dataHora)
            put("valorTotal", venda.valorTotal)
        }

        val id = db.insert(DBHelper.TABLE_VENDAS, null, values)
        db.close()
        return id
    }

}