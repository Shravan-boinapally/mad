package com.example.waterminder.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class AdviceSlip(val slip: Advice)
data class Advice(val id: Int, val advice: String)

interface TipsApi {
    @GET("advice")
    suspend fun getRandomTip(): AdviceSlip

    companion object {
        private const val BASE_URL = "https://api.adviceslip.com/"

        fun create(): TipsApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TipsApi::class.java)
        }
    }
}
