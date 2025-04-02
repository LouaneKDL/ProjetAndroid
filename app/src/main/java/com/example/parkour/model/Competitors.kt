package com.example.parkour.model

/**
 * Represents the attributes to request to register a competitor
 *
 * @property id id competitor
 * @property created_at competitor creation date
 * @property updated_at date of last competitor modification
 * @property first_name first name competitor
 * @property last_name  last name competitor
 * @property email email address competitor
 * @property phone  phone competitor
 * @property born_at birth date competitor
 * @property gender gender competitor
 *
 */
data class Competitors(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val born_at: String,
    val gender: String
)