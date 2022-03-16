package me.chaoticwagon.ecomc.events.custom

import net.minestom.server.event.ListenerHandle
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance

class DayNightChangeEvent(private val instance : Instance) : InstanceEvent {
    override fun getInstance(): Instance {
        return instance
    }

    

}