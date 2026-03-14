package com.movtrans.movtrackinglite.model

import java.io.Serializable

// 1. Objeto principal da Resposta da API
data class EntregaColetaResponse(
    val success: Boolean,
    val message: String,
    val dados: EntregaColetaDados
)

// 2. Objeto que contém as duas listas
data class EntregaColetaDados(
    val coleta: List<Movimentacao>,
    val entrega: List<Movimentacao>
)

// 3. Objeto de cada item (Híbrido: Entrega ou Coleta)
data class Movimentacao(
    val codMov: Int,
    val codigoVeiculo: Int,
    val razao: String,
    val tipo: Int,
    val endereco: String,
    val bairro: String,
    val numero: String,
    val cidade: String,
    val telefone: String,
    val dataHora: String?,
    val ndoc: String,
    val canhoto: Int,
    val notasFiscais: List<NotaFiscal>
) : Serializable

// 4. Objeto da Nota Fiscal vinculada
data class NotaFiscal(
    val codigoNota: Int,
    val numeroNota: Int,
    val chaveNota: String
) : Serializable