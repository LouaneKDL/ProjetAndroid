package com.example.parkour.model

data class PerformancesRequest (
    val competitor_id: Int,
    val course_id: Int,
    val status: String,
    val total_time: Int,
)