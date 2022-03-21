package me.chaoticwagon.ecomc.events

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

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

        if (message.startsWith("yes")) {
            val scroll = ItemStack.builder(Material.PAPER)
                .amount(1)
                .displayName(Component.text("Land Claim Scroll", NamedTextColor.AQUA))
                .lore(Component.text("Use in an unclaimed chunk to claim it.", NamedTextColor.WHITE))
                .build()
            event.player.inventory.addItemStack(scroll)
        }

        return EventListener.Result.SUCCESS
    }
}