package com.movtrans.movtrackinglite.service

import com.movtrans.movtrackinglite.model.LoginRequest
import com.movtrans.movtrackinglite.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/login.php")
    fun realizarLogin(@Body request: LoginRequest): Call<LoginResponse>
}