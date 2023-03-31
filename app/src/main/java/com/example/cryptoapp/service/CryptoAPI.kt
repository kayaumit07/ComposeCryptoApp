package com.example.cryptoapp.service

import com.example.cryptoapp.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {
    @GET("sapi/v1/tickers/24hr")
    fun getData():Call<List<CryptoModel>>
}