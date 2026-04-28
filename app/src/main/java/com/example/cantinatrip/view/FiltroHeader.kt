package com.example.cantinatrip.view

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.example.cantinatrip.R

class FiltroHeader(
    rootView: View,
    private val onFiltrosChanged: () -> Unit
) {
    private data class FaixaValor(val label: String, val valorMinimo: Double)

    private val context = rootView.context

    private val btnIconBusca: ImageButton = rootView.findViewById(R.id.btnIconBusca)
    private val btnIconValor: ImageButton = rootView.findViewById(R.id.btnIconValor)
    private val btnIconTodos: ImageButton = rootView.findViewById(R.id.btnIconTodos)
    private val tvHintFiltro: TextView = rootView.findViewById(R.id.tvHintFiltro)
    private val btnVoltarLimpar: ImageButton = rootView.findViewById(R.id.btnVoltarLimpar)
    private val btnTodosFiltro: ImageButton = rootView.findViewById(R.id.btnTodosFiltro)
    private val searchView: SearchView = rootView.findViewById(R.id.searchView)
    private val spinnerValor: Spinner = rootView.findViewById(R.id.spinnerValor)

    private val faixasValor = listOf(
        FaixaValor("Todos os valores", 0.0),
        FaixaValor("Acima de R$ 10", 10.0),
        FaixaValor("Acima de R$ 20", 20.0),
        FaixaValor("Acima de R$ 50", 50.0),
        FaixaValor("Acima de R$ 100", 100.0)
    )

    val textoBusca: String
        get() = searchView.query?.toString().orEmpty()

    val valorMinimoFiltro: Double
        get() = faixasValor[spinnerValor.selectedItemPosition].valorMinimo

    private var pronto = false

    init {
        configurarListeners()
        configurarSearchView()
        configurarSpinner()
        mostrarEstadoInicial()
        pronto = true
    }

    private fun notificarMudanca() {
        if (pronto) onFiltrosChanged()
    }

    private fun configurarListeners() {
        btnIconBusca.setOnClickListener { mostrarEstadoBuscaNome() }
        btnIconValor.setOnClickListener { mostrarEstadoFiltroValor() }
        btnIconTodos.setOnClickListener { mostrarEstadoInicial() }
        btnVoltarLimpar.setOnClickListener { mostrarEstadoInicial() }
        btnTodosFiltro.setOnClickListener { mostrarEstadoInicial() }
    }

    private fun configurarSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                notificarMudanca()
                return true
            }
        })
    }

    private fun configurarSpinner() {
        val labels = faixasValor.map { it.label }
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            labels
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerValor.adapter = spinnerAdapter

        spinnerValor.aoSelecionarItem { notificarMudanca() }
    }

    private fun Spinner.aoSelecionarItem(acao: () -> Unit) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = acao()
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun mostrarEstadoInicial() {
        btnIconBusca.visibility = View.VISIBLE
        btnIconValor.visibility = View.VISIBLE
        btnIconTodos.visibility = View.VISIBLE

        tvHintFiltro.visibility = View.GONE
        tvHintFiltro.text = ""
        btnVoltarLimpar.visibility = View.GONE
        btnTodosFiltro.visibility = View.GONE

        searchView.visibility = View.GONE
        spinnerValor.visibility = View.GONE

        searchView.setQuery("", false)
        spinnerValor.setSelection(0)

        notificarMudanca()
    }

    private fun mostrarEstadoBuscaNome() {
        btnIconBusca.visibility = View.GONE
        btnIconValor.visibility = View.GONE
        btnIconTodos.visibility = View.GONE

        tvHintFiltro.visibility = View.VISIBLE
        tvHintFiltro.text = context.getString(R.string.hint_busca_nome)
        btnVoltarLimpar.visibility = View.VISIBLE
        btnTodosFiltro.visibility = View.VISIBLE

        searchView.visibility = View.VISIBLE
        spinnerValor.visibility = View.GONE
    }

    private fun mostrarEstadoFiltroValor() {
        btnIconBusca.visibility = View.GONE
        btnIconValor.visibility = View.GONE
        btnIconTodos.visibility = View.GONE

        tvHintFiltro.visibility = View.VISIBLE
        tvHintFiltro.text = context.getString(R.string.hint_filtro_valor)
        btnVoltarLimpar.visibility = View.VISIBLE
        btnTodosFiltro.visibility = View.VISIBLE

        searchView.visibility = View.GONE
        spinnerValor.visibility = View.VISIBLE
    }
}
