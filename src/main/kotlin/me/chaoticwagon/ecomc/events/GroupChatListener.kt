package me.chaoticwagon.ecomc.events

import me.chaoticwagon.ecomc.claiming.Group
import me.chaoticwagon.ecomc.formatMinimessage
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerChatEvent

class GroupChatListener: EventListener<PlayerChatEvent> {
    override fun eventType(): Class<PlayerChatEvent> {
        return PlayerChatEvent::class.java
    }

    private var queue: HashMap<Player, Player> = HashMap()


    override fun run(event: PlayerChatEvent): EventListener.Result {
        val message = event.message
        val sender = event.player

        if (message.startsWith("group")){
            val args = message.split(" ")
            if (args[1] == "invite"){
                val target: Player = event.player.instance!!.players.find { it.name == Component.text(args[2])}!!
                if(sender == target){
                    sender.sendMessage(
                        "<red>You can't invite yourself to a group.".formatMinimessage()
                    )
                    return EventListener.Result.SUCCESS
                }

                //if the sender hasn't made a group yet
                if (groups.find {it.owner == sender} == null){
                    val newGroup = Group(sender, sender.username)
                    newGroup.addMember(sender, sender)
                    groups.add(newGroup)
                    sender.sendMessage(
                        "<green>Made a group under your name since you aren't in one. You can change the name of the group with <white>/group rename (name)".formatMinimessage()

                    )
                }

                queue[target] = sender
                target.sendMessage(
                    "<aqua>You have been invited to join the group ${sender.group.name} by ${sender.username}. Type <white>/group accept ${sender.username} <aqua>to join.".formatMinimessage()
                )
                return EventListener.Result.SUCCESS
            }

            if (args[1] == "accept"){
                val target: Player = event.player.instance!!.players.find { it.name == Component.text(args[2])}!!
                if (!queue.containsKey(sender)){
                    sender.sendMessage(
                        "<red>You have not been invited to join a group.".formatMinimessage()
                    )
                    return EventListener.Result.SUCCESS
                }

                if (queue[sender] != target){
                    sender.sendMessage(
                        "<red>That player has not invited you to join their group".formatMinimessage()
                    )
                    return EventListener.Result.SUCCESS
                }
                queue.remove(sender)
                groups.find { it.getMember(target) == target }!!.addMember(target, sender)

                sender.group.broadcast(
                    "<aqua>${sender.username} has joined the group invited by ${target.username}.".formatMinimessage()
                )

                return EventListener.Result.SUCCESS
            }

            if (args[1] == "leave"){
                sender.group.broadcast(
                    "<aqua>${sender.username} <white>has left the group.".formatMinimessage()
                )
                sender.group.removeMember(sender)
                return EventListener.Result.SUCCESS
            }

            if (args[1] == "kick"){
                val target: Player = event.player.instance!!.players.find { it.name == Component.text(args[2])}!!
                sender.group.broadcast(
                    "<aqua>${sender.username} <white>has kicked ${args[2]} from the group.".formatMinimessage()
                )
                sender.group.removeMember(target)
                return EventListener.Result.SUCCESS
            }

            if (args[1] == "rename"){
                val newName = args.drop(2).joinToString(" ")
                sender.group.name = newName
                sender.group.broadcast(
                    "<aqua>${sender.username} <white>has renamed the group to $newName.".formatMinimessage()
                )
                return EventListener.Result.SUCCESS
            }

            if (args[1] == "info"){
                sender.sendMessage(
                    "<aqua>Group name: ${sender.group.name}".formatMinimessage()
                )
                sender.sendMessage(
                    "<aqua>Group owner: ${sender.group.owner.username}".formatMinimessage()
                )
                sender.sendMessage(
                    "<aqua>Group members: ${sender.group.members.joinToString(", ") { it.username }}".formatMinimessage()
                )
                return EventListener.Result.SUCCESS
            }

        }

        return EventListener.Result.SUCCESS
    }


}
var groups: MutableList<Group> = mutableListOf()
val Player.group: Group
    get() = groups.find { it.getMember(this) == this }!!
val Player.hasGroup: Boolean
    get() = groups.find { it.getMember(this) == this } != null
