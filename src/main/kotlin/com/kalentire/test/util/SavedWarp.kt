package com.kalentire.test.util

import kotlinx.serialization.Serializable

@Serializable
data class SavedWarp (
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float ,
    val pitch: Float,
    val name: String
) {
    override fun toString(): String {
        return "$name | $x,$y,$z"
    }
}

