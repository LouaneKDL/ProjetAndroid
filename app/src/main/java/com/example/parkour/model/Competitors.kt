package com.example.parkour.model

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