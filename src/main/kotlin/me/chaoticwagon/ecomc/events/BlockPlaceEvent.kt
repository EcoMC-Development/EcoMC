package me.chaoticwagon.ecomc.events

import me.chaoticwagon.ecomc.claiming.ClaimHandler
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerBlockPlaceEvent

class BlockPlaceEvent(private val claimHandler: ClaimHandler) : EventListener<PlayerBlockPlaceEvent> {

    override fun eventType(): Class<PlayerBlockPlaceEvent> {
        return PlayerBlockPlaceEvent::class.java
    }

    override fun run(event: PlayerBlockPlaceEvent): EventListener.Result {
        val player = event.player
        val instance = event.player.instance!!
        val pos = event.blockPosition
        val chunk = instance.getChunkAt(pos)!!

        if (!claimHandler.isOwner(player, chunk)) {
            event.isCancelled = true
            return EventListener.Result.SUCCESS
        }
        return EventListener.Result.SUCCESS
    }
}
