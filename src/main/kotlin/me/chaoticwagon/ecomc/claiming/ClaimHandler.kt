package me.chaoticwagon.ecomc.claiming

import me.chaoticwagon.ecomc.formatMinimessage
import net.minestom.server.entity.Player
import net.minestom.server.instance.Chunk
import net.minestom.server.tag.Tag

class ClaimHandler {

    fun handleClaim(player: Player, chunk: Chunk) {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: ""

        if (chunkOwner.isEmpty()){
            chunk.setTag(Tag.String("chunk-owner"), player.uuid.toString())
            player.sendMessage(
                "<green>You claimed this chunk!".formatMinimessage()
            )
            return
        }

        if (chunkOwner != player.uuid.toString()){
            player.sendMessage(
                "<red>This chunk is already claimed!".formatMinimessage()
            )
            return
        }
        if (chunkOwner == player.uuid.toString()){
            player.sendMessage(
                "<green>You already own this chunk!".formatMinimessage()
            )
            return
        }
    }
}