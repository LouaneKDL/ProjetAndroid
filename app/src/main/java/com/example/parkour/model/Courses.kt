package com.example.parkour.model


/**
 * Represents the course attributes
 *
 * @property id course identifier
 * @property created_at course creation date
 * @property updated_at date of last course modification
 * @property name course name
 *  @property max_duration maximum duration course
 *  @property position order of the course in the competition program
 *  @property is_over indicates whether the race is over
 *  @property competition_id id of the competitions included in the obstacle request
 *
 */
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