package com.example.cantinatrip.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cantinatrip.R
import java.util.Locale

class TotalActivity : AppCompatActivity() {

    private lateinit var quantidadeVendasEditText: EditText
    private lateinit var valorTotalEditText: EditText
    private lateinit var voltarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val valorTotalVendas = intent.getDoubleExtra("totalDinheiro", 0.0)
        val quantidadeTotalVendas = intent.getIntExtra("totalQuantidade", 0)

        quantidadeVendasEditText = findViewById(R.id.editTextVendas)
        valorTotalEditText = findViewById(R.id.editTextValorTotal)
        voltarButton = findViewById(R.id.btnVoltar)

        quantidadeVendasEditText.setText(quantidadeTotalVendas.toString())
        valorTotalEditText.setText(
            String.format(Locale.getDefault(), "R$ %.2f", valorTotalVendas)
        )

        voltarButton.setOnClickListener {
            finish()
        }
    }
}