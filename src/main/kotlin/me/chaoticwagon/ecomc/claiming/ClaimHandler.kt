package me.chaoticwagon.ecomc.claiming

import me.chaoticwagon.ecomc.events.group
import me.chaoticwagon.ecomc.events.hasGroup
import me.chaoticwagon.ecomc.formatMinimessage
import net.minestom.server.entity.Player
import net.minestom.server.instance.Chunk
import net.minestom.server.tag.Tag
import java.util.*
import kotlin.collections.HashMap

class ClaimHandler {

    //temporary data storage for player claims
    private var claimedLand : HashMap<UUID, Int> = HashMap()

    fun handleClaim(player: Player, chunk: Chunk) {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: ""
        val uuid: UUID = if (player.hasGroup) player.group.uuid else player.uuid

        if (chunkOwner.isEmpty()){

            if (!claimedLand.containsKey(uuid)) {
                claimedLand[uuid] = 1
                chunk.setTag(Tag.String("chunk-owner"), uuid.toString())
                player.sendMessage(
                    "<green>You claimed this chunk!".formatMinimessage()
                )
                player.inventory.itemInMainHand = player.inventory.itemInMainHand.withAmount(player.inventory.itemInMainHand.amount - 1)

                return
            }
            if (claimedLand[uuid]!! >= 5){
                player.sendMessage(
                    "<red> You cannot claim more than 5 chunks".formatMinimessage()
                )
                return
            }

            chunk.setTag(Tag.String("chunk-owner"), uuid.toString())
            claimedLand[uuid] = claimedLand[uuid]!! + 1
            player.inventory.itemInMainHand = player.inventory.itemInMainHand.withAmount(player.inventory.itemInMainHand.amount - 1)
            player.sendMessage(
                "<green>You claimed this chunk!".formatMinimessage()
            )

            return
        }

        if (chunkOwner != uuid.toString()){
            player.sendMessage(
                "<red>This chunk is already claimed!".formatMinimessage()
            )
            return
        }

        if (chunkOwner == uuid.toString()) {
            player.sendMessage(
                "<green>You already own this chunk!".formatMinimessage()
            )
            return
        }
    }

    fun isOwner(player: Player, chunk: Chunk): Boolean {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: ""
        if (chunk.hasTag(Tag.String("chunk-owner"))){
            if(player.hasGroup){
                return chunkOwner == player.group.uuid.toString()
            }
            return chunkOwner == player.uuid.toString()
        }
        return true
    }


}