package com.example.parkour.model

data class CompetitionRequest(
    val name : String,
    val age_min : Int,
    val age_max : Int,
    val gender : String,
    val has_retry : Boolean
)