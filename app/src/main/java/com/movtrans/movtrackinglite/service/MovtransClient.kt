package com.movtrans.movtrackinglite.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovtransClient {
    private const val BASE_URL = "https://movtracking.movtrans.com.br/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}