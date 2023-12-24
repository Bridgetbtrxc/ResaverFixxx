package com.elflin.movieapps.retrofit
import com.elflin.movieapps.data.EndPointAPI
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: EndPointAPI by lazy {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8000/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndPointAPI::class.java)
    }
}