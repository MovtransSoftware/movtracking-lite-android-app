package com.movtrans.movtrackinglite.model

data class LoginRequest(
    val aliases: String,
    val imei: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val dados: Any?
)

