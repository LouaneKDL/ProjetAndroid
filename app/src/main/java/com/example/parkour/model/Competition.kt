package com.example.parkour.model

data class Competition (
    val id: Int,
    val created_at: String,
    val updated_at : String,
    val name : String,
    val age_min : Int,
    val age_max : Int,
    val gender : String,
    val has_retry : Int,
    val status : String
)