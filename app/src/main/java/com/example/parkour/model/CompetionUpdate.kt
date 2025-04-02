package com.example.parkour.model

data class CompetionUpdate(
    val name : String,
    val age_min : Int,
    val age_max : Int,
    val gender : String,
    val has_retry : Boolean,
    val status : String
)