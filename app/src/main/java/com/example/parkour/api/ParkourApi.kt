package com.example.parkour.api

import com.example.parkour.model.Competition
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import com.example.parkour.model.Obstacles
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ParkourApi {

    //COMPETITIONS

    @GET("/api/competitions")
    suspend fun getCompetitions(): Response<List<Competition>>

    @GET("/api/competitions/{id}")
    suspend fun getCompetitionById(@Path("id") id:Int):Response<Competition>

    @GET("/api/competitions/{id}/inscriptions")
    suspend fun getInscriptionsByCompetitionId(@Path("id")id:Int):Response<List<Competitors>>

    @GET("/api/competitions/{id}/courses")
    suspend fun getCoursesByCompetitionId(@Path("id")id:Int):Response<List<Courses>>

    //COMPETITORS

    @GET("/api/competitors")
    suspend fun getCompetitors(): Response<List<Competitors>>

    @GET("/api/competitors/{id}")
    suspend fun getCompetitor(@Path("id") id:Int): Response<Competitors>

    @GET("/api/competitors/{id}/performances")
    suspend fun getPerformanceForACompetitor(@Path("id")id:Int): Response<Performances>

    @GET("/api/competitors/{id}/courses")
    suspend fun getCoursesByACompetitorId(@Path("id")id:Int): Response<List<Courses>>

    @GET("/api/competitors/{id}/{id_course}/details_performances")
    suspend fun getPerformanceDetailsByIdCompetitor(@Path("id")id:Int) : Response<Performance_obstacles>

    //COURSES

    @GET("/api/courses")
    suspend fun getCourses(): Response<List<Courses>>

    @GET("/api/courses/{id}/obstacles")
    suspend fun getObstaclesByCourseId(@Path("id")id: Int) : Response<List<Obstacles>>

    @GET("/api/courses/{id}/performances")
    suspend fun getPerformancesByCourseId(@Path("id")id: Int) : Response<List<Performances>>

    @GET("/api/courses/{id}")
    suspend fun getCourseById(@Path("id")id: Int): Response<Courses>

    //OBSTACLES

    @GET("/api/obstacles")
    suspend fun getObstacles(): Response<List<Obstacles>>

    @GET("/api/obstacles/{id}")
    suspend fun getObstacleById(@Path("id")id: Int): Response<Obstacles>

    //PERFORMANCE OBSTACLES

    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles(): Response<List<Performance_obstacles>>

    @GET("/api/performance_obstacles/{id}")
    suspend fun getPerformanceObstaclesById(id: Int) : Response<Performance_obstacles>

    //PERFORMANCES

    @GET("/api/performances")
    suspend fun getPerformances(): Response<List<Performances>>

    @GET("/api/performances/{id}")
    suspend fun getPerformancesById(id:Int): Response<Performances>

    @GET("/api/performances/{id}/details")
    suspend fun getDetailsByPerformancesById(id:Int): Response<List<Performance_obstacles>>

}