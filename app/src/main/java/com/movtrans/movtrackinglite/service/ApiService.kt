package com.movtrans.movtrackinglite.service

import com.movtrans.movtrackinglite.model.EntregaColetaResponse
import com.movtrans.movtrackinglite.model.LoginRequest
import com.movtrans.movtrackinglite.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login.php")
    fun realizarLogin(@Body request: LoginRequest): Call<LoginResponse>

    // Método para buscar entregas e coletas usando Bearer Token
    @GET("api/buscarEntregasColetas.php")
    fun buscarEntregasColetas(
        @Header("Authorization") token: String
    ): Call<EntregaColetaResponse>
}