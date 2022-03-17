package me.chaoticwagon.ecomc.claiming

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.instance.Chunk
import net.minestom.server.tag.Tag

class ClaimHandler {

    fun handleClaim(player: Player, chunk: Chunk) {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: {
            chunk.setTag(Tag.String("chunk-owner"), player.uuid.toString())
        }

        if (chunkOwner != player.uuid.toString()){
            player.sendMessage(Component.text("This land is already claimed"))
        }
        if (chunkOwner == player.uuid.toString()){
            player.sendMessage(Component.text("You have already claimed this land"))
        }
    }

}