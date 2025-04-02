package com.example.parkour.model


/**
 * Represents the performance obstacle attributes
 *
 * @property id performance  identifier
 * @property competitor_id identifier of the competitor
 * @property course_id identifier of the course
 * @property status current status of the performance
 * @property total_time total time taken to complete the course
 * @property created_at performance creation date
 * @property updated_at date of last performance modification
 */

data class Performances (
    val id: Int,
    val competitor_id: Int,
    val course_id: Int,
    val status: String,
    val total_time: Int,
    val created_at: String,
    val updated_at: String
)