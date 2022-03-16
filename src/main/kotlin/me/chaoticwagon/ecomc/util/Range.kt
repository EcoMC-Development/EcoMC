package me.chaoticwagon.ecomc.util

class Range(private val min: Number, private val max: Number) {
    fun contains(value: Number): Boolean {
        return value.toDouble() in min.toDouble()..max.toDouble() // Convert to double for widest range of numbers
    }
}