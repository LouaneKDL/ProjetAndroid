package com.example.parkour.model

/**
 * Represents the competitions attributes
 *
 * @property id competition identifier
 * @property created_at competition creation date
 * @property updated_at date of last competition modification
 * @property name competition name
 * @property age_min minimum age required to participate in the competition.
 * @property age_max  maximum age allowed to participate in the competition.
 * @property gender gender category for the competition
 * @property has_retry if you get a second chance
 * @property status competition status
 *
 */
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