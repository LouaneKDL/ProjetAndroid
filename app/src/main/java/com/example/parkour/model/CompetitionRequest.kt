package com.example.parkour.model

/**
 * Represents the competitions request attributes
 *
 * @property name competition request name
 * @property age_min minimum age required to participate in the competition.
 * @property age_max  maximum age allowed to participate in the competition.
 * @property gender gender category for the competition
 * @property has_retry if you get a second chance
 * @property status competition status
 *
 */
data class CompetitionRequest (
    val name : String,
    val age_min : Int,
    val age_max : Int,
    val gender : String,
    val has_retry : Boolean
)