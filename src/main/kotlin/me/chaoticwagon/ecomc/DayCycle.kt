package me.chaoticwagon.ecomc

import me.chaoticwagon.ecomc.events.custom.DayNightChangeEvent
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.timer.TaskSchedule
import java.time.Duration

class DayCycle(private val world: InstanceContainer, private val eventNode: EventNode<InstanceEvent>) {
    init {
        start()
    }

    private fun start(){
        var isDay = true
        val schedulerManager = MinecraftServer.getSchedulerManager()
        schedulerManager.scheduleTask(
            {
                world.time += 1

                if (world.time >= 24000) {
                    world.time = 0
                }

                if (world.time in 13000..22999 && isDay) { // extra checks cuz sunset and sunrise aren't exactly 12000, and 24000
                    isDay = false
                    eventNode.call(DayNightChangeEvent(world, DayNightChangeEvent.DayTime.NIGHT))
                } else if (world.time >= 23000 && !isDay) {
                    isDay = true
                    eventNode.call(DayNightChangeEvent(world, DayNightChangeEvent.DayTime.DAY))
                }
            },
            TaskSchedule.duration(Duration.ofMillis(50)), // 50ms = 1 tick
            TaskSchedule.duration(Duration.ofSeconds(50))
        )
    }

    companion object {
        val Instance.isDay: Boolean
            get() = time < 13000
        val Instance.dayTime: DayNightChangeEvent.DayTime
            get() = if (isDay) DayNightChangeEvent.DayTime.DAY else DayNightChangeEvent.DayTime.NIGHT
    }
}