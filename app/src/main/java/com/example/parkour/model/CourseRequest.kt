package com.example.parkour.model

data class CourseRequest(
    val name: String,
    val max_duration : Int,
    val competition_id: Int
)