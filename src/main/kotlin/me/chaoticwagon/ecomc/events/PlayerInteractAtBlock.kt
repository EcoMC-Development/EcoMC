package me.chaoticwagon.ecomc.events

import me.chaoticwagon.ecomc.claiming.ClaimHandler
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerBlockInteractEvent

class PlayerInteractAtBlock(private val claimHandler: ClaimHandler) : EventListener<PlayerBlockInteractEvent> {


    override fun eventType(): Class<PlayerBlockInteractEvent> {
        return PlayerBlockInteractEvent::class.java
    }

    override fun run(event: PlayerBlockInteractEvent): EventListener.Result {
        if(event.hand == Player.Hand.OFF) return EventListener.Result.SUCCESS

        val player = event.player
        val pos = event.blockPosition
        val instance = player.instance!!
        val chunk = instance.getChunkAt(pos)!!
        claimHandler.handleClaim(player, chunk)

        return EventListener.Result.SUCCESS
    }
}