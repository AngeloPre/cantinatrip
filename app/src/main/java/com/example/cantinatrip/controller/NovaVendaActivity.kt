package com.example.cantinatrip.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cantinatrip.R

class NovaVendaActivity : AppCompatActivity() {

    lateinit var item1Text: TextView
    lateinit var item2Text: TextView
    lateinit var item3Text: TextView
    lateinit var item4Text: TextView
    lateinit var nomeEditText: EditText
    lateinit var totalEditText: EditText
    lateinit var btnSalvar: Button
    lateinit var btnExcluir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_venda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        item1Text = findViewById(R.id.textView_qty_1)
        item2Text = findViewById(R.id.textView_qty_2)
        item3Text = findViewById(R.id.textView_qty_3)
        item4Text = findViewById(R.id.textView_qty_4)

        nomeEditText = findViewById(R.id.editTextNome)
        totalEditText = findViewById(R.id.editTextSubtotal)

        btnSalvar = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)

    }
}