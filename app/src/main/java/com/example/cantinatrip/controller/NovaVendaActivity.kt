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

    private lateinit var tituloEditText: TextView

    private lateinit var btnSalvar: Button
    private lateinit var btnAcaoSecundaria: Button

    private val quantities = mutableMapOf<Int, Int>()

    private lateinit var vendaDAO: VendaDAO
    private lateinit var itemVendaDAO: ItemVendaDAO

    private var vendaIdEdicao: Int = -1

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

        vendaIdEdicao = intent.getIntExtra("vendaId", -1)

        inicializarComponentes()
        configurarModoTela()
        configurarAcoes()
        atualizarTotal()
    }

    private fun inicializarComponentes() {
        item1QtyText = findViewById(R.id.textView_qty_1)
        item2QtyText = findViewById(R.id.textView_qty_2)
        item3QtyText = findViewById(R.id.textView_qty_3)
        item4QtyText = findViewById(R.id.textView_qty_4)
        tituloEditText = findViewById(R.id.textViewTitulo)

        nomeEditText = findViewById(R.id.editTextNome)
        totalEditText = findViewById(R.id.editTextSubtotal)

        btnSalvar = findViewById(R.id.btnSalvar)
        btnAcaoSecundaria = findViewById(R.id.btnExcluir)
    }

    private fun configurarModoTela() {
        if (vendaIdEdicao != -1) {
            carregarDadosParaEdicao()
            btnAcaoSecundaria.text = "Excluir"
            tituloEditText.text = "Editar Venda"
        } else {
            btnAcaoSecundaria.text = "Cancelar"
        }
    }

    private fun carregarDadosParaEdicao() {
        val venda = vendaDAO.getVendaById(vendaIdEdicao)

        if (venda != null) {
            nomeEditText.setText(venda.nomeComprador)

            val itens = itemVendaDAO.getByVendaId(vendaIdEdicao)

            for (item in itens) {
                quantities[item.produtoId] = item.quantidade

                when (item.produtoId) {
                    4 -> item1QtyText.text = item.quantidade.toString()
                    1 -> item2QtyText.text = item.quantidade.toString()
                    2 -> item3QtyText.text = item.quantidade.toString()
                    3 -> item4QtyText.text = item.quantidade.toString()
                }
            }
        }
    }

    private fun configurarAcoes() {
        configurarContadorProduto(4, R.id.button_minus_1, R.id.button_plus_1, item1QtyText)
        configurarContadorProduto(1, R.id.button_minus_2, R.id.button_plus_2, item2QtyText)
        configurarContadorProduto(2, R.id.button_minus_3, R.id.button_plus_3, item3QtyText)
        configurarContadorProduto(3, R.id.button_minus_4, R.id.button_plus_4, item4QtyText)

        btnSalvar.setOnClickListener {
            salvarVenda()
        }

        btnAcaoSecundaria.setOnClickListener {
            if (vendaIdEdicao != -1) {
                excluirVenda()
            } else {
                finish()
            }
        }
    }

    private fun configurarContadorProduto(
        productId: Int,
        minusId: Int,
        plusId: Int,
        qtyTextView: TextView
    ) {
        if (!quantities.containsKey(productId)) {
            quantities[productId] = 0
        }

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
        val nome = nomeEditText.text.toString().trim()

        if (nome.isEmpty()) {
            nomeEditText.error = "Obrigatório"
            nomeEditText.requestFocus()
            return
        }

        val itensSelecionados = buscarItensSelecionados()

        if (itensSelecionados.isEmpty()) {
            Toast.makeText(this, "Selecione um item", Toast.LENGTH_SHORT).show()
            return
        }

        val totalFinal = calcularTotal(itensSelecionados)

        val dataHora = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        if (vendaIdEdicao == -1) {
            criarNovaVenda(nome, dataHora, totalFinal, itensSelecionados)
        } else {
            atualizarVendaExistente(nome, dataHora, totalFinal, itensSelecionados)
        }
    }

    private fun criarNovaVenda(
        nome: String,
        dataHora: String,
        totalFinal: Double,
        itensSelecionados: Map<Int, Int>
    ) {
        val novaVenda = Venda(
            nomeComprador = nome,
            dataHora = dataHora,
            valorTotal = totalFinal
        )

        val vendaId = vendaDAO.addVenda(novaVenda)

        if (vendaId <= 0) {
            Toast.makeText(this, "Erro ao salvar a venda", Toast.LENGTH_SHORT).show()
            return
        }

        salvarItensDaVenda(vendaId.toInt(), itensSelecionados)

        Toast.makeText(this, "Venda registrada com sucesso", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun atualizarVendaExistente(
        nome: String,
        dataHora: String,
        totalFinal: Double,
        itensSelecionados: Map<Int, Int>
    ) {
        val vendaEditada = Venda(
            id = vendaIdEdicao,
            nomeComprador = nome,
            dataHora = dataHora,
            valorTotal = totalFinal
        )

        val linhasAfetadas = vendaDAO.updateVenda(vendaEditada)

        if (linhasAfetadas <= 0) {
            Toast.makeText(this, "Erro ao atualizar a venda", Toast.LENGTH_SHORT).show()
            return
        }

        itemVendaDAO.deleteByVendaId(vendaIdEdicao)
        salvarItensDaVenda(vendaIdEdicao, itensSelecionados)

        Toast.makeText(this, "Venda atualizada com sucesso", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun salvarItensDaVenda(vendaId: Int, itensSelecionados: Map<Int, Int>) {
        for ((produtoId, quantidade) in itensSelecionados) {
            val produto = buscarProdutoPorId(produtoId)

            if (produto != null) {
                val item = ItemVenda(
                    vendaId = vendaId,
                    produtoId = produtoId,
                    quantidade = quantidade,
                    subtotal = produto.valor * quantidade
                )

                itemVendaDAO.insert(item)
            }
        }
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

    private fun excluirVenda() {
        val linhasAfetadas = vendaDAO.deleteVenda(vendaIdEdicao)

        if (linhasAfetadas > 0) {
            Toast.makeText(this, "Excluído", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_SHORT).show()
        }
    }
}