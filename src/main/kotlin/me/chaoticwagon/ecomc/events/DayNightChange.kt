package me.chaoticwagon.ecomc.events

import me.chaoticwagon.ecomc.events.custom.DayNightChangeEvent
import net.minestom.server.event.EventListener

class DayNightChange: EventListener<DayNightChangeEvent> {
    override fun eventType(): Class<DayNightChangeEvent> {
        return DayNightChangeEvent::class.java
    }

    override fun run(event: DayNightChangeEvent): EventListener.Result {
        if(event.changedTo() == DayNightChangeEvent.DayTime.NIGHT) {
            println("IT IS NIGHT!!")
        }else{
            println("IT IS DAY!!")
        }
        return EventListener.Result.SUCCESS
    }
}