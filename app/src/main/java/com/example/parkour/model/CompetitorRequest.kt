package com.example.parkour.model

data class CompetitorRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val born_at: String
)