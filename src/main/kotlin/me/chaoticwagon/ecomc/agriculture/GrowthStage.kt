package me.chaoticwagon.ecomc.agriculture

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.metadata.other.ItemFrameMeta.Orientation
import net.minestom.server.instance.Instance

class GrowthStage {
    private lateinit var placedAt: Pos
    private lateinit var placedOrient: Orientation

    fun place(location: Pos, orientation: Orientation) {
        if(orientation == Orientation.UP || orientation == Orientation.DOWN) {
            throw IllegalArgumentException("Orientation cannot be UP or DOWN")
        }
        placedOrient = orientation
        placedAt = location
    }

    fun remove(instance: Instance) {

    }
}