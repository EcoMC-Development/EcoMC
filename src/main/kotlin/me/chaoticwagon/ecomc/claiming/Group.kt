package me.chaoticwagon.ecomc.claiming

import me.chaoticwagon.ecomc.Constants
import me.chaoticwagon.ecomc.formatMinimessage
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import java.util.*

class Group (val owner: Player, var name: String){

    val uuid: UUID = UUID.randomUUID()

    var members: MutableList<Player> = mutableListOf()

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