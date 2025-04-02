package com.example.parkour.model


/**
 * Represents the obstacle course attributes
 *
 * @property id  identifier of obstacle of the course
 * @property obstacle_id course obstacle id
 * @property obstacle_name course obstacle name
 * @property position order of obstacles in the course
 */
data class ObstacleCourse (
    val id:Int,
    val obstacle_id:Int,
    val obstacle_name: String,
    val position: Int
)