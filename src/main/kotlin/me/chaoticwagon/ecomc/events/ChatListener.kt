package me.chaoticwagon.ecomc.events

import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerChatEvent

class ChatListener: EventListener<PlayerChatEvent> {
    override fun eventType(): Class<PlayerChatEvent> {
        return PlayerChatEvent::class.java
    }

    override fun run(event: PlayerChatEvent): EventListener.Result {
        val message = event.message
        if(message.startsWith("sus")){
            val args = message.split(" ").reversed().dropLast(1).reversed()
            println(args[0])
            event.player.instance!!.time = args[0].toInt().toLong()
        }
        return EventListener.Result.SUCCESS
    }
}