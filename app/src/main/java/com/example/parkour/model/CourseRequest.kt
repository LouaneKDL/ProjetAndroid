package com.example.parkour.model

/**
 * Represents the attributes to request to register a course
 *
 * @property name name of the course request
 * @property max_duration maximum duration course request
 * @property competition_id id of the competitions included in the obstacle request
 */
data class CourseRequest(
    val name: String,
    val max_duration : Int,
    val competition_id: Int
)