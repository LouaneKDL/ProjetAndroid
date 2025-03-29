package com.example.parkour.model

data class Performance_obstacles(
    val id: Int,
    val obstacle_id: Int,
    val performance_id: Int,
    val has_fell: Int,
    val to_verify: Int,
    val time: Int,
    val updated_at: String,
    val created_at: String
)