package me.chaoticwagon.ecomc.claiming

import me.chaoticwagon.ecomc.Constants
import me.chaoticwagon.ecomc.formatMinimessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*
import kotlin.collections.HashMap

class Group (var owner: Player, var name: String){


    var landClaimed: Int = 0
    val uuid: UUID = UUID.randomUUID()
    var members: MutableList<Player> = mutableListOf()
    var redeemableClaims: Int = 0
    private var contributions: HashMap<Player, Int> = HashMap()
    var groupClaimMax: Int = members.size * Constants.PLAYER_CLAIM_MAX


    fun addMember(sender: Player, newMember: Player){
        if (members.contains(newMember)){
            sender.sendMessage(
                "<red>${newMember.username} is already a member of this group.".formatMinimessage()
            )
            return
        }
        if (members.size >= Constants.GROUP_SIZE){
            sender.sendMessage(
                "<red> The group is full!".formatMinimessage()
            )
            return
        }

        members.add(newMember)
    }

    fun removeMember(player: Player){
        members.remove(player)
        val scroll = ItemStack.builder(Material.PAPER)
            .amount(contributions[player]!!)
            .displayName(Component.text("Land Claim Scroll", NamedTextColor.AQUA))
            .lore(Component.text("Use in an unclaimed chunk to claim it.", NamedTextColor.WHITE))
            .build()
        player.inventory.addItemStack(scroll)
        contributions.remove(player)
    }

    fun getMember(member: Player): Player? {
        return members.find { it == member }
    }

    fun broadcast(message: Component){
        members.forEach{
            if (it.isOnline) it.sendMessage(message)
        }
    }

}