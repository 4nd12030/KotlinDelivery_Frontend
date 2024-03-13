package com.andy.kotlindelivery.api

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun getClient(url: String) : Retrofit {

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getClientWebToken(url: String, token: String): Retrofit {
        val client = OkHttpClient.Builder()
       client.addInterceptor{ chain ->
           val request= chain.request()
           val  newRequest =request.newBuilder().header("Authorization", token)
           chain.proceed(newRequest.build())
        }

        return Retrofit.Builder()
            .baseUrl(url)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}