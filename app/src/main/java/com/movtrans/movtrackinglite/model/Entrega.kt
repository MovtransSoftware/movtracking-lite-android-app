package com.movtrans.movtrackinglite.model

import java.io.Serializable

data class Entrega(
    val id: String,
    val nome: String,
    val endereco: String,
    val telefone: String?,
    // Novos campos adicionados abaixo:
    val cteNumero: String?,
    val cteChave: String?,
    val nfeNumero: String?,
    var status: String = "Pendente"
) : Serializable