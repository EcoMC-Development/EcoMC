package me.chaoticwagon.ecomc.events

import me.chaoticwagon.ecomc.claiming.ClaimHandler
import me.chaoticwagon.ecomc.claiming.Group
import me.chaoticwagon.ecomc.formatMinimessage
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerChatEvent

class GroupChatListener(private val claimHandler:ClaimHandler): EventListener<PlayerChatEvent> {
    override fun eventType(): Class<PlayerChatEvent> {
        return PlayerChatEvent::class.java
    }

    private var queue: HashMap<Player, Player> = HashMap()


    override fun run(event: PlayerChatEvent): EventListener.Result {
        val message = event.message
        val sender = event.player

        if (message.startsWith("unclaim")){
            claimHandler.unclaim(sender)
        }

        return EventListener.Result.SUCCESS
    }


}
var groups: MutableList<Group> = mutableListOf()
val Player.group: Group
    get() = groups.find { it.getMember(this) == this }!!
val Player.hasGroup: Boolean
    get() = groups.find { it.getMember(this) == this } != null
