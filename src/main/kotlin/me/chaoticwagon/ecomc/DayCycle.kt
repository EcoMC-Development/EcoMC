package me.chaoticwagon.ecomc

import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.timer.TaskSchedule
import java.time.Duration

class DayCycle(private val world: InstanceContainer) {

    init {
        start()
    }

    private fun start(){
        var isDay = true
        val schedulerManager = MinecraftServer.getSchedulerManager()
        schedulerManager.scheduleTask(
            {

                if (world.time >= 12000 && isDay) {
                    isDay = false
                    TODO("Call event that it is midnight")
                }
                if (world.time < 12000 && !isDay) {
                    isDay = true
                    TODO("Call event that it is day")
                }
                if (world.time >= 24000) {
                    world.time = 0
                }

                world.time += 2000
            },
            TaskSchedule.duration(Duration.ofSeconds(1)),
            TaskSchedule.duration(Duration.ofSeconds(1))
        )
    }
}