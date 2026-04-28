package com.example.cantinatrip.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.cantinatrip.data.db.DBHelper
import com.example.cantinatrip.model.Venda

class VendaDAO(private val context: Context) {
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

    fun getVendaById(id: Int): Venda? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DBHelper.TABLE_VENDAS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var venda: Venda? = null

        if (cursor.moveToFirst()) {
            venda = Venda(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nomeComprador = cursor.getString(cursor.getColumnIndexOrThrow("nome_comprador")),
                dataHora = cursor.getString(cursor.getColumnIndexOrThrow("dataHora")),
                valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
            )
        }

        cursor.close()
        db.close()
        return venda
    }

    fun getAllVendas(): List<Venda> {
        val lista = mutableListOf<Venda>()
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DBHelper.TABLE_VENDAS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val venda = Venda(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nomeComprador = cursor.getString(cursor.getColumnIndexOrThrow("nome_comprador")),
                    dataHora = cursor.getString(cursor.getColumnIndexOrThrow("dataHora")),
                    valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
                )
                lista.add(venda)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun updateVenda(venda: Venda): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nome_comprador", venda.nomeComprador)
            put("dataHora", venda.dataHora)
            put("valorTotal", venda.valorTotal)
        }

        val rows = db.update(
            DBHelper.TABLE_VENDAS,
            values,
            "id = ?",
            arrayOf(venda.id.toString())
        )

        db.close()
        return rows
    }

    fun deleteVenda(id: Int): Int {
        val db = dbHelper.writableDatabase
        val itemVendaDAO = ItemVendaDAO(context)

        itemVendaDAO.deleteByVendaId(id)

        val rows = db.delete(
            DBHelper.TABLE_VENDAS,
            "id = ?",
            arrayOf(id.toString())
        )

        db.close()
        return rows
    }

    fun getVendasByNomeComprador(nomeComprador: String): List<Venda> {
        val lista = mutableListOf<Venda>()
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DBHelper.TABLE_VENDAS,
            null,
            "nome_comprador LIKE ?",
            arrayOf("%$nomeComprador%"),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val venda = Venda(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nomeComprador = cursor.getString(cursor.getColumnIndexOrThrow("nome_comprador")),
                    dataHora = cursor.getString(cursor.getColumnIndexOrThrow("dataHora")),
                    valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
                )
                lista.add(venda)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun getByFaixaValor(min: Double, max: Double): List<Venda> {
        val lista = mutableListOf<Venda>()
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DBHelper.TABLE_VENDAS,
            null,
            "valorTotal BETWEEN ? AND ?",
            arrayOf(min.toString(), max.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val venda = Venda(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nomeComprador = cursor.getString(cursor.getColumnIndexOrThrow("nome_comprador")),
                    dataHora = cursor.getString(cursor.getColumnIndexOrThrow("dataHora")),
                    valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
                )
                lista.add(venda)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun getAllOrdenado(criterio: String): List<Venda> {
        val lista = mutableListOf<Venda>()
        val db = dbHelper.readableDatabase

        val orderBy = when (criterio) {
            "nome" -> "nome_comprador ASC"
            "valor" -> "valorTotal ASC"
            "data" -> "dataHora DESC"
            else -> "id DESC"
        }

        val cursor = db.query(
            DBHelper.TABLE_VENDAS,
            null,
            null,
            null,
            null,
            null,
            orderBy
        )

        if (cursor.moveToFirst()) {
            do {
                val venda = Venda(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nomeComprador = cursor.getString(cursor.getColumnIndexOrThrow("nome_comprador")),
                    dataHora = cursor.getString(cursor.getColumnIndexOrThrow("dataHora")),
                    valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
                )
                lista.add(venda)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }


}
