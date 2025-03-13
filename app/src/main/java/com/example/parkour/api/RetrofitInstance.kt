package com.example.parkour.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASEURL = "http://92.222.217.100/"

    private fun getInstance(): ParkourApi{
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("Goj3C4DbSXKssHZsg9134KCtwdLvo4RHDrBwgLTsmnj8CHcZWsYIB3kk6iaOjuXO"))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ParkourApi::class.java)

    }

    val parkourApi : ParkourApi = getInstance()


}