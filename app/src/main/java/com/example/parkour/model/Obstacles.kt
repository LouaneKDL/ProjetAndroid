package com.example.parkour.model

import android.net.Uri
/**
 * Represents the obstacles attributes
 *
 * @property id obstacle identifier
 * @property created_at obstacle creation date
 * @property updated_at date of last obstacle modification
 * @property name obstacle name
*/
data class Obstacles(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val name: String

  /*  val id: Int,
    val name: String,
    val picture: String*/
)