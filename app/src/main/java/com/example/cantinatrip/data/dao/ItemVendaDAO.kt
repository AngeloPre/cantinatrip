package com.example.cantinatrip.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.cantinatrip.data.db.DBHelper
import com.example.cantinatrip.model.ItemVenda

class ItemVendaDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun insert(item: ItemVenda): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("vendaId", item.vendaId)
            put("produtoId", item.produtoId)
            put("quantidade", item.quantidade)
            put("subtotal", item.subtotal)
        }
        val id = db.insert(DBHelper.TABLE_ITENS, null, values)
        db.close()
        return id
    }

    fun getByVendaId(vendaId: Int): List<ItemVenda> {
        val itens = mutableListOf<ItemVenda>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_ITENS,
            null,
            "vendaId = ?",
            arrayOf(vendaId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val item = ItemVenda(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    vendaId = cursor.getInt(cursor.getColumnIndexOrThrow("vendaId")),
                    produtoId = cursor.getInt(cursor.getColumnIndexOrThrow("produtoId")),
                    quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade")),
                    subtotal = cursor.getDouble(cursor.getColumnIndexOrThrow("subtotal"))
                )
                itens.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itens
    }

    fun deleteByVendaId(vendaId: Int): Int {
        val db = dbHelper.writableDatabase
        val rows = db.delete(DBHelper.TABLE_ITENS, "vendaId = ?", arrayOf(vendaId.toString()))
        db.close()
        return rows
    }
}
