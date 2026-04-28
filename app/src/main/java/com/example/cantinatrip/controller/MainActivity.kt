package com.example.cantinatrip.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cantinatrip.R
import com.example.cantinatrip.adapter.VendasAdapter
import com.example.cantinatrip.data.dao.ItemVendaDAO
import com.example.cantinatrip.data.dao.VendaDAO
import com.example.cantinatrip.model.Produto
import com.example.cantinatrip.model.Venda

class MainActivity : AppCompatActivity() {

    private lateinit var vendaDAO: VendaDAO
    private lateinit var itemVendaDAO: ItemVendaDAO
    private lateinit var recyclerViewVendas: RecyclerView
    private lateinit var tvVazio: TextView
    private lateinit var searchView: SearchView
    private lateinit var adapter: VendasAdapter
    private var listaVendasFull: List<Venda> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vendaDAO = VendaDAO(this)
        itemVendaDAO = ItemVendaDAO(this)

        tvVazio = findViewById(R.id.tvVazio)
        recyclerViewVendas = findViewById(R.id.vendasRV)
        recyclerViewVendas.layoutManager = LinearLayoutManager(this)
        recyclerViewVendas.setHasFixedSize(true)
        recyclerViewVendas.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        adapter = VendasAdapter(
            vendas = emptyList(),
            context = this,
            click = { venda ->
                val intent = Intent(this, NovaVendaActivity::class.java).apply {
                    putExtra("vendaId", venda.id)
                }
                startActivity(intent)
            },
            previewProvider = { venda -> montarPreview(venda) }
        )
        recyclerViewVendas.adapter = adapter

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText)
                return true
            }
        })

        val btnNovaVenda = findViewById<Button>(R.id.button)
        btnNovaVenda.setOnClickListener {
            val intent = Intent(this, NovaVendaActivity::class.java)
            startActivity(intent)
        }

        listarVendas()
    }

    override fun onResume() {
        super.onResume()
        listarVendas()
    }

    private fun listarVendas() {
        listaVendasFull = vendaDAO.getAllOrdenado("nome")
        filtrar(searchView.query.toString())
    }

    private fun filtrar(texto: String?) {
        val listaFiltrada = if (texto.isNullOrEmpty()) {
            listaVendasFull
        } else {
            listaVendasFull.filter { it.nomeComprador.contains(texto, ignoreCase = true) }
        }

        if (listaFiltrada.isEmpty()) {
            recyclerViewVendas.visibility = View.GONE
            tvVazio.visibility = View.VISIBLE
        } else {
            recyclerViewVendas.visibility = View.VISIBLE
            tvVazio.visibility = View.GONE
        }
        
        adapter.updateList(listaFiltrada)
    }

    private fun montarPreview(venda: Venda): String {
        val itens = itemVendaDAO.getByVendaId(venda.id)
        return itens.mapNotNull { item ->
            Produto.getById(item.produtoId)?.let { produto ->
                "${item.quantidade}x ${produto.nome}"
            }
        }.joinToString(", ")
    }
}
