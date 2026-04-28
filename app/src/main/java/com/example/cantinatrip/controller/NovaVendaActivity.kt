package com.example.cantinatrip.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cantinatrip.R
import com.example.cantinatrip.data.dao.ItemVendaDAO
import com.example.cantinatrip.data.dao.VendaDAO
import com.example.cantinatrip.model.ItemVenda
import com.example.cantinatrip.model.Produto
import com.example.cantinatrip.model.Venda
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NovaVendaActivity : AppCompatActivity() {

    private lateinit var item1QtyText: TextView
    private lateinit var item2QtyText: TextView
    private lateinit var item3QtyText: TextView
    private lateinit var item4QtyText: TextView

    private lateinit var nomeEditText: EditText
    private lateinit var totalEditText: EditText

    private lateinit var btnSalvar: Button
    private lateinit var btnExcluir: Button

    private val quantities = mutableMapOf<Int, Int>()

    private lateinit var vendaDAO: VendaDAO
    private lateinit var itemVendaDAO: ItemVendaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_venda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vendaDAO = VendaDAO(this)
        itemVendaDAO = ItemVendaDAO(this)

        inicializarComponentes()
        configurarAcoes()
        atualizarTotal()
    }

    private fun inicializarComponentes() {
        item1QtyText = findViewById(R.id.textView_qty_1)
        item2QtyText = findViewById(R.id.textView_qty_2)
        item3QtyText = findViewById(R.id.textView_qty_3)
        item4QtyText = findViewById(R.id.textView_qty_4)

        nomeEditText = findViewById(R.id.editTextNome)
        totalEditText = findViewById(R.id.editTextSubtotal)

        btnSalvar = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)
    }

    private fun configurarAcoes() {
        /*
            IDs dos produtos:
            1 = Coxinha
            2 = Pastel
            3 = Café
            4 = Bombom

            Layout:
            linha 1 = Bombom
            linha 2 = Coxinha
            linha 3 = Pastel
            linha 4 = Café
        */

        configurarContadorProduto(
            productId = 4,
            minusId = R.id.button_minus_1,
            plusId = R.id.button_plus_1,
            qtyTextView = item1QtyText
        )

        configurarContadorProduto(
            productId = 1,
            minusId = R.id.button_minus_2,
            plusId = R.id.button_plus_2,
            qtyTextView = item2QtyText
        )

        configurarContadorProduto(
            productId = 2,
            minusId = R.id.button_minus_3,
            plusId = R.id.button_plus_3,
            qtyTextView = item3QtyText
        )

        configurarContadorProduto(
            productId = 3,
            minusId = R.id.button_minus_4,
            plusId = R.id.button_plus_4,
            qtyTextView = item4QtyText
        )

        btnSalvar.setOnClickListener {
            salvarVenda()
        }

        btnExcluir.setOnClickListener {
            finish()
        }
    }

    private fun configurarContadorProduto(
        productId: Int,
        minusId: Int,
        plusId: Int,
        qtyTextView: TextView
    ) {
        quantities[productId] = 0

        val btnMais = findViewById<Button>(plusId)
        val btnMenos = findViewById<Button>(minusId)

        btnMais.setOnClickListener {
            val quantidadeAtual = quantities.getOrDefault(productId, 0)
            val novaQuantidade = quantidadeAtual + 1

            quantities[productId] = novaQuantidade
            qtyTextView.text = novaQuantidade.toString()

            atualizarTotal()
        }

        btnMenos.setOnClickListener {
            val quantidadeAtual = quantities.getOrDefault(productId, 0)

            if (quantidadeAtual > 0) {
                val novaQuantidade = quantidadeAtual - 1

                quantities[productId] = novaQuantidade
                qtyTextView.text = novaQuantidade.toString()

                atualizarTotal()
            }
        }
    }

    private fun atualizarTotal() {
        val total = calcularTotal(quantities)

        totalEditText.setText(
            String.format(Locale.getDefault(), "R$ %.2f", total)
        )
    }

    private fun calcularTotal(itens: Map<Int, Int>): Double {
        var total = 0.0

        for ((produtoId, quantidade) in itens) {
            val produto = buscarProdutoPorId(produtoId)

            if (produto != null) {
                total += produto.valor * quantidade
            }
        }

        return total
    }

    private fun salvarVenda() {
        val nomeComprador = nomeEditText.text.toString().trim()

        if (nomeComprador.isEmpty()) {
            Toast.makeText(
                this,
                "O nome do comprador é obrigatório",
                Toast.LENGTH_SHORT
            ).show()

            nomeEditText.requestFocus()
            return
        }

        val itensSelecionados = buscarItensSelecionados()

        if (itensSelecionados.isEmpty()) {
            Toast.makeText(
                this,
                "Selecione pelo menos um item",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val valorTotalVenda = calcularTotal(itensSelecionados)

        val dataHora = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val venda = Venda(
            nomeComprador = nomeComprador,
            dataHora = dataHora,
            valorTotal = valorTotalVenda
        )

        val vendaId = vendaDAO.addVenda(venda)

        if (vendaId <= 0) {
            Toast.makeText(
                this,
                "Erro ao salvar a venda no banco",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        for ((produtoId, quantidade) in itensSelecionados) {
            val produto = buscarProdutoPorId(produtoId)

            if (produto == null) {
                Toast.makeText(
                    this,
                    "Produto não encontrado",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            val item = ItemVenda(
                vendaId = vendaId.toInt(),
                produtoId = produtoId,
                quantidade = quantidade,
                subtotal = produto.valor * quantidade
            )

            itemVendaDAO.insert(item)
        }

        Toast.makeText(
            this,
            "Venda registrada com sucesso!",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun buscarItensSelecionados(): Map<Int, Int> {
        val itensSelecionados = mutableMapOf<Int, Int>()

        for ((produtoId, quantidade) in quantities) {
            if (quantidade > 0) {
                itensSelecionados[produtoId] = quantidade
            }
        }

        return itensSelecionados
    }

    private fun buscarProdutoPorId(id: Int): Produto? {
        for (produto in Produto.lista) {
            if (produto.id == id) {
                return produto
            }
        }

        return null
    }
}