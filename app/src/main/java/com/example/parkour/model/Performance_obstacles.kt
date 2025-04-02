package com.example.parkour.model

/**
 * Represents the performance obstacle attributes
 *
 * @property id performance obstacle identifier
 * @property obstacle_id identifier of the obstacle
 * @property has_fell indicates whether the competitor fell during the obstacle
 * @property to_verify indicates whether the performance needs to be verified
 * @property time he time taken to complete the obstacle
 * @property created_at competition creation date
 * @property updated_at date of last competition modification
 */
data class Performance_obstacles(
    val id: Int,
    val obstacle_id: Int,
    val performance_id: Int,
    val has_fell: Int,
    val to_verify: Int,
    val time: Int,
    val updated_at: String,
    val created_at: String
)