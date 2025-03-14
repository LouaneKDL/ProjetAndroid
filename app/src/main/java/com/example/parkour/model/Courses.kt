package com.example.parkour.model

data class Courses(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val max_duration : Int,
    val position: Int,
    val is_over: Int,
    val competition_id: Int
)