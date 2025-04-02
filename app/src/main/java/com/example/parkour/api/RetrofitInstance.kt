package com.example.parkour.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * this class configures Retrofit to make authenticated API calls to a specified server, using an authentication token to secure the requests.
 *
 */
object RetrofitInstance {

    private const val BASEURL = "http://92.222.217.100/"

    /**
     * @return ParkourApi with an HTTP client that adds an authentication token to each request
     */
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