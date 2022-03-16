package me.chaoticwagon.ecomc

import me.chaoticwagon.ecomc.Constants.Companion.DAY_SUNRISE
import me.chaoticwagon.ecomc.Constants.Companion.DAY_SUNSET
import me.chaoticwagon.ecomc.events.custom.DayNightChangeEvent
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.timer.TaskSchedule
import java.time.Duration

class DayCycle(private val world: InstanceContainer, private val eventNode: EventNode<InstanceEvent>) {
    private var isRunning = false

    fun start(){
        if(isRunning) return

        isRunning = true
        var isDay = true

        val schedulerManager = MinecraftServer.getSchedulerManager()
        schedulerManager.scheduleTask(
            {
                world.time += 1

                if (world.time >= 24000) {
                    world.time = 0
                }

                if (world.time in DAY_SUNSET until DAY_SUNRISE && isDay) { // extra checks cuz sunset and sunrise aren't exactly 12000, and 24000. Basically redefine day/night
                    isDay = false
                    eventNode.call(DayNightChangeEvent(world, DayNightChangeEvent.DayTime.NIGHT))
                } else if (world.time >= DAY_SUNRISE && !isDay) {
                    isDay = true
                    eventNode.call(DayNightChangeEvent(world, DayNightChangeEvent.DayTime.DAY))
                }
            },
            TaskSchedule.duration(Duration.ofMillis(50)), // 50ms = 1 tick
            TaskSchedule.duration(Duration.ofMillis(50))
        )
    }

    companion object {
        val Instance.isDay: Boolean
            get() = time < DAY_SUNSET
        val Instance.dayTime: DayNightChangeEvent.DayTime
            get() = if (isDay) DayNightChangeEvent.DayTime.DAY else DayNightChangeEvent.DayTime.NIGHT
    }
}