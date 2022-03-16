package me.chaoticwagon.ecomc.events.custom

import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance

class DayNightChangeEvent(private val instance: Instance, private val changedTo: DayTime) : InstanceEvent {
    override fun getInstance(): Instance {
        return instance
    }

    fun changedTo(): DayTime {
        return changedTo
    }

    enum class DayTime {
        DAY,
        NIGHT
    }

}