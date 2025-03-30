package com.example.parkour.model

data class Performances (
    val id: Int,
    val competitor_id: Int,
    val course_id: Int,
    val status: String,
    val total_time: Int,
    val created_at: String,
    val updated_at: String
)