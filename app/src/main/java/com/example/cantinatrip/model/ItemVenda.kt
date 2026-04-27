package com.example.cantinatrip.model

data class ItemVenda(
    val id: Int = 0,
    val vendaId: Int,
    val produtoId: Int,
    val quantidade: Int,
    val subtotal: Double
)
