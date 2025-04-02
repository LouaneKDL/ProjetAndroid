package com.example.parkour.model

/**
 * Represents the attributes to a request to register a competitor
 *
 * @property first_name first name competitor request
 * @property last_name  last name competitor request
 * @property email email address competitor request
 * @property phone  phone competitor request
 * @property gender gender competitor request
 * @property born_at birth date competitor request
 */
data class CompetitorRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val born_at: String
)