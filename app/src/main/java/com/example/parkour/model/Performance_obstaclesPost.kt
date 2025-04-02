package com.example.parkour.model

data class Performance_obstaclesPost (
    val obstacle_id: Int,
    val performance_id: Int,
    val has_fell: Int,
    val to_verify: Int,
    val time: Int
)