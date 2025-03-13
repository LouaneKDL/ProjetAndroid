package com.example.parkour.api

import com.example.parkour.model.Competition
import retrofit2.Response
import retrofit2.http.GET

interface ParkourApi {

    @GET("/api/competitions")
    suspend fun getCompetitions(): Response<List<Competition>>
}