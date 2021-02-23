package com.example.VocabularyBook

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://192.168.43.178:3000"
    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            while (retrofit == null) {
                retrofit= Retrofit.Builder().baseUrl(BASE_URL) .addConverterFactory(GsonConverterFactory.create()).build();

            }

            return retrofit!!
        }
}