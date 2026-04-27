package com.example.cantinatrip.model

import com.example.cantinatrip.R

data class Produto(
    val id: Int,
    val nome: String,
    val valor: Double,
    val imagemResId: Int
) {
    companion object {
        val lista: List<Produto> = listOf(
            Produto(1, "Coxinha",  6.00, R.drawable.coxinha),
            Produto(2, "Pastel",    7.50, R.drawable.pastel),
            Produto(3, "Café",  3.00, R.drawable.cafe),
            Produto(4, "Bombom",       2.00, R.drawable.bombom)
        )
        fun getById(id: Int): Produto? = lista.find { it.id == id }
    }
}
