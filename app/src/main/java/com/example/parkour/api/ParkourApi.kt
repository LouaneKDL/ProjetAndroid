package com.example.parkour.api

import com.example.parkour.model.Competition
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import com.example.parkour.model.Obstacles
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import retrofit2.Response
import retrofit2.http.GET

interface ParkourApi {

    @GET("/api/competitions")
    suspend fun getCompetitions(): Response<List<Competition>>

    @GET("/api/competitors")
    suspend fun getCompetitors(): Response<List<Competitors>>

    @GET("/api/courses")
    suspend fun getCourses(): Response<List<Courses>>

    @GET("/api/obstacles")
    suspend fun getObstacles(): Response<List<Obstacles>>

    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles(): Response<List<Performance_obstacles>>

    @GET("/api/performances")
    suspend fun getPerformances(): Response<List<Performances>>

}