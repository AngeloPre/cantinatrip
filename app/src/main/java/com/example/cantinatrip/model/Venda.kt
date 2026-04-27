package com.example.cantinatrip.model

data class Venda(
    val id: Int = 0,
    val nomeComprador: String,
    val dataHora: String,
    val valorTotal: Double
) {
    override fun toString(): String {
        return "$nomeComprador — R$ ${"%.2f".format(valorTotal)}"
    }
}