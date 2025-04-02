package com.example.parkour.api

import com.example.parkour.model.Competition
import com.example.parkour.model.CompetitionRequest
import com.example.parkour.model.CompetitorRequest
import com.example.parkour.model.Competitors
import com.example.parkour.model.CourseRequest
import com.example.parkour.model.Courses
import com.example.parkour.model.ObstacleCourse
import com.example.parkour.model.ObstacleNoDate
import com.example.parkour.model.ObstaclePost
import com.example.parkour.model.Obstacles
import com.example.parkour.model.ObstaclesCourses
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performance_obstaclesRequest
import com.example.parkour.model.Performances
import com.example.parkour.model.PerformancesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ParkourApi {

    //RESET BDD
    @GET("/api/reset")
    suspend fun reset(): Response<Unit>

    //COMPETITIONS

    @GET("/api/competitions")
    suspend fun getCompetitions(): Response<List<Competition>>

    @GET("/api/competitions/{id}")
    suspend fun getCompetitionById(@Path("id") id:Int):Response<Competition>

    @GET("/api/competitions/{id}/inscriptions")
    suspend fun getInscriptionsByCompetitionId(@Path("id")id:Int):Response<List<Competitors>>

    @GET("/api/competitions/{id}/courses")
    suspend fun getCoursesByCompetitionId(@Path("id")id:Int):Response<List<Courses>>

    @POST("/api/competitions")
    suspend fun postCompetitions(@Body competition: CompetitionRequest): Response<Competition>

    data class CompetitourRequest(val competitor_id: Int)
    @POST("/api/competitions/{id}/add_competitor")
    suspend fun postCompetitorsToCompetitionById(
        @Path("id")id:Int,
        @Body request: CompetitourRequest
    ): Response<Competitors>


    @PUT("/api/competitions/{id}")
    suspend fun putCompetition(@Path("id")id: Int,@Body competition: Competition): Response<Competition>

    @DELETE("/api/competitions/{id}")
    suspend fun deleteCompetitionById(@Path("id")id: Int): Response<Unit>

    @DELETE("/api/competitions/{id}/remove_competitor/{id_competitor}")
    suspend fun deleteCompetitorFromCompetition(@Path("id")id: Int, @Path("id_competitor")idCompetitor: Int): Response<Unit>

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

    @POST("/api/competitors")
    suspend fun postCompetitors(@Body competitorRequest: CompetitorRequest): Response<CompetitorRequest>

    @PUT("/api/competitors/{id}")
    suspend fun putCompetitors(@Path("id")id: Int,@Body competitors: Competitors): Response<Competitors>

    @DELETE("/api/competitors/{id}")
    suspend fun deleteCompetitor(@Path("id")id:Int): Response<Unit>

    //COURSES

    @GET("/api/courses")
    suspend fun getCourses(): Response<List<Courses>>

    @GET("/api/courses/{id}/obstacles")
    suspend fun getObstaclesByCourseId(@Path("id")id: Int) : Response<List<ObstacleCourse>>

    @GET("/api/courses/{id}/performances")
    suspend fun getPerformancesByCourseId(@Path("id")id: Int) : Response<List<Performances>>

    @GET("/api/courses/{id}")
    suspend fun getCourseById(@Path("id")id: Int): Response<Courses>

    @POST("/api/courses")
    suspend fun postCourse(@Body courses: CourseRequest): Response<Courses>

    @POST("/api/courses/{id}/add_obstacle")
    suspend fun postObstacleToCourseById(@Path("id")id: Int, @Body obstacle: ObstaclePost): Response<Obstacles>

    @POST("/api/courses/{id}/update_obstacle_position")
    suspend fun postObstacleInCertainPositionByCourseId(@Path("id")id:Int, @Body obstacles: Obstacles): Response<Obstacles>

    @PUT("/api/courses/{id}")
    suspend fun putCourses(@Path("id")id: Int,@Body courses: Courses): Response<Courses>

    @DELETE("/api/courses/{id}")
    suspend fun deleteCourse(@Path("id")id: Int): Response<Unit>

    @DELETE("/api/courses/{id}/remove_obstacle/{id_obstacle}")
    suspend fun deleteObstacleFromCourse(@Path("id")id: Int, @Path("id_obstacle")idObstacle: Int): Response<Unit>

    //OBSTACLES

    @GET("/api/obstacles")
    suspend fun getObstacles(): Response<List<Obstacles>>

    @GET("/api/obstacles/{id}")
    suspend fun getObstacleById(@Path("id")id: Int): Response<Obstacles>

    @POST("/api/obstacles")
    suspend fun postObstacle(@Body obstacles: ObstacleNoDate): Response<Obstacles>

    @PUT("/api/obstacles/{id}")
    suspend fun putObstacles(@Path("id")id: Int,@Body obstacles: Obstacles): Response<Obstacles>

    @DELETE("/api/obstacles/{id}")
    suspend fun deleteObstacle(@Path("id")id: Int): Response<Unit>

    //PERFORMANCE OBSTACLES

    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles(): Response<List<Performance_obstacles>>

    @GET("/api/performance_obstacles/{id}")
    suspend fun getPerformanceObstaclesById(@Path("id")id: Int) : Response<Performance_obstacles>

    @POST("/api/performance_obstacles")
    suspend fun postPerformanceObstacles(@Body performanceObstacles: Performance_obstacles): Response<Performance_obstacles>

    @PUT("/api/performance_obstacles/{id}")
    suspend fun putPerformanceObstacles(@Path("id")id: Int,@Body performanceObstacles: Performance_obstaclesRequest): Response<Performance_obstacles>

    //PERFORMANCES

    @GET("/api/performances")
    suspend fun getPerformances(): Response<List<Performances>>

    @GET("/api/performances/{id}")
    suspend fun getPerformancesById(@Path("id")id:Int): Response<Performances>

    @GET("/api/performances/{id}/details")
    suspend fun getDetailsByPerformancesById(@Path("id")id:Int): Response<List<Performance_obstacles>>

    @POST("/api/performances")
    suspend fun postPerformances(@Body performances: PerformancesRequest): Response<Performances>

    @PUT("/api/performances/{id}")
    suspend fun putPerformances(@Path("id")id: Int,@Body performances: PerformancesRequest): Response<Performances>

    @DELETE("/api/performances/{id}")
    suspend fun deletePerformance(@Path("id")id: Int): Response<Unit>

}