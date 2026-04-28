package com.example.cantinatrip.model

data class Venda(
    val id: Int = 0,
    val nomeComprador: String,
    val dataHora: String,
    val valorTotal: Double
)