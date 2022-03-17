package me.chaoticwagon.ecomc.agriculture

import me.chaoticwagon.ecomc.util.Range

interface IPlant {
    val name: String
    val id: Int
    val tempTolerance: Range
    val moistureTolerance: Range

    val stages: List<GrowthStage>
}