package me.chaoticwagon.ecomc.claiming

import me.chaoticwagon.ecomc.formatMinimessage
import net.minestom.server.entity.Player
import net.minestom.server.instance.Chunk
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag

class ClaimHandler {

    //temporary data storage for player claims
    private var claimedLand : HashMap<Player, Int> = HashMap()


    fun handleClaim(player: Player, chunk: Chunk) {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: ""

        if (chunkOwner.isEmpty()){


            if (!claimedLand.containsKey(player)) {
                claimedLand[player] = 1
                chunk.setTag(Tag.String("chunk-owner"), player.uuid.toString())
                player.sendMessage(
                    "<green>You claimed this chunk!".formatMinimessage()
                )
                player.inventory.itemInMainHand = player.inventory.itemInMainHand.withAmount(player.inventory.itemInMainHand.amount - 1)

                return
            }
            if (claimedLand[player]!! >= 5){
                player.sendMessage(
                    "<red> You cannot claim more than 5 chunks".formatMinimessage()
                )
                return
            }

            chunk.setTag(Tag.String("chunk-owner"), player.uuid.toString())
            claimedLand[player] = claimedLand[player]!! + 1
            player.inventory.itemInMainHand = player.inventory.itemInMainHand.withAmount(player.inventory.itemInMainHand.amount - 1)
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

    fun isOwner(player: Player, chunk: Chunk): Boolean {
        val chunkOwner = chunk.getTag(Tag.String("chunk-owner")) ?: ""
        if (chunk.hasTag(Tag.String("chunk-owner"))){
            return chunkOwner == player.uuid.toString()
        }
        return true
    }


}