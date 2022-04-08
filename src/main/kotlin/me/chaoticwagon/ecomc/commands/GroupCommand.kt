package me.chaoticwagon.ecomc.commands

import me.chaoticwagon.ecomc.claiming.ClaimHandler
import me.chaoticwagon.ecomc.claiming.Group
import me.chaoticwagon.ecomc.events.group
import me.chaoticwagon.ecomc.events.groups
import me.chaoticwagon.ecomc.events.hasGroup
import me.chaoticwagon.ecomc.formatMinimessage
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder

class GroupCommand(private val claimHandler: ClaimHandler) : Command("group", "g") {

    private var queue: HashMap<Player, Player> = HashMap()
    private val target: ArgumentEntity = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true)

    init {
        defaultExecutor =
            CommandExecutor { sender: CommandSender, _: CommandContext? ->
                sender.sendMessage(
                    "Usage: /group <string>"
                )
            }
        val commandArgument = ArgumentType.String("command")

        val rename = ArgumentType.String("rename")

        addSyntax(this::invite, commandArgument, target)
        addSyntax(this::accept, commandArgument, target)
        addSyntax(this::leave, commandArgument)
        addSyntax(this::kick, commandArgument, target)
        addSyntax(this::rename, commandArgument, rename)
        addSyntax(this::info, commandArgument)
        addSyntax(this::transfer, commandArgument, target)
    }

    private fun invite(sender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        if (!arg.equals("invite")) return

        val target = context.get<EntityFinder>("target").findFirstPlayer(sender) ?: return

        val player = sender as Player


        if(player == target){
            player.sendMessage(
                "<red>You can't invite yourself to a group.".formatMinimessage()
            )
            return
        }
        //if the sender hasn't made a group yet
        if (!player.hasGroup){
            val newGroup = Group(player, player.username)
            newGroup.addMember(player, player)
            groups.add(newGroup)
            player.sendMessage(
                "<green>Made a group under your name since you aren't in one. You can change the name of the group with <aqua>/group rename (name)".formatMinimessage()

            )
        }
        queue[target] = player
        target.sendMessage(
            "<aqua>You have been invited to join the group ${player.group.name} by ${player.username}. Type <white>/group accept ${player.username} <aqua>to join.".formatMinimessage()
        )
    }

    private fun accept(commandSender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        println("yes")
        if (!arg.equals("accept")) return

        val sender = commandSender as Player
        val target = context.get<EntityFinder>("target").findFirstPlayer(sender) ?: return

        println("accept command 1")
        if (!queue.containsKey(sender)){
            sender.sendMessage(
                "<red>You have not been invited to join a group.".formatMinimessage()
            )
            println("accept command 2")
            return
        }

        if (queue[sender] != target){
            sender.sendMessage(
                "<red>That player has not invited you to join their group".formatMinimessage()
            )
            println("accept command 3")

            return
        }
        println("accept command 4")
        queue.remove(sender)
        groups.find { it.getMember(target) == target }!!.addMember(target, sender)
        claimHandler.transferClaims(sender)
        sender.group.broadcast(
            "<aqua>${sender.username} has joined the group invited by ${target.username}.".formatMinimessage()
        )

    }

    private fun leave(commandSender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        if (!arg.equals("leave")) return

        val sender = commandSender as Player

        if (sender == sender.group.owner){
            sender.sendMessage(
                "<red>You can't leave your own group. Transfer ownership to another person to leave.".formatMinimessage()
            )
            return
        }

        sender.group.broadcast(
            "<aqua>${sender.username} <white>has left the group.".formatMinimessage()
        )
        sender.group.removeMember(sender)
    }

    private fun kick(commandSender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        if (!arg.equals("kick")) return

        val sender = commandSender as Player
        val target = context.get<EntityFinder>("target").findFirstPlayer(sender) ?: return

        if (sender == sender.group.owner){
            sender.group.broadcast(
                "<aqua>${sender.username} <white>has kicked ${target.username} from the group.".formatMinimessage()
            )
            sender.group.removeMember(target)
        }else{
            sender.sendMessage(
                "<red>You can't kick people from a group you don't own.".formatMinimessage()
            )

        }
    }

    private fun rename(commandSender: CommandSender, context: CommandContext) {
        val arg = context.get<String>("command")
        if (!arg.equals("rename")) return

        val sender = commandSender as Player
        val newName = context.get<String>("rename")

        if (sender == sender.group.owner){
            sender.group.name = newName
            sender.group.broadcast(
                "<aqua>${sender.username} <white>has renamed the group to $newName.".formatMinimessage()
            )
        }else{
            sender.sendMessage(
                "<red>You must be the owner of the group to rename it.".formatMinimessage()
            )
        }
    }

    private fun info (commandSender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        if (!arg.equals("info")) return

        val sender = commandSender as Player
        sender.sendMessage(
            "<aqua>Group name: ${sender.group.name}".formatMinimessage()
        )
        sender.sendMessage(
            "<aqua>Group owner: ${sender.group.owner.username}".formatMinimessage()
        )
        sender.sendMessage(
            "<aqua>Group members: ${sender.group.members.joinToString(", ") { it.username }}".formatMinimessage()
        )
    }

    private fun transfer(commandSender: CommandSender, context: CommandContext){
        val arg = context.get<String>("command")
        if (!arg.equals("transfer")) return

        val sender = commandSender as Player
        val target = context.get<EntityFinder>("target").findFirstPlayer(sender) ?: return

        if (!sender.group.members.contains(target)){
            sender.sendMessage(
                "<red>You can't transfer ownership to someone who isn't in your group.".formatMinimessage()
            )
        }


        if (sender == sender.group.owner){
            sender.group.owner = target
            sender.group.broadcast(
                "<aqua>${sender.username} <white>has transferred ownership to ${target.username}.".formatMinimessage()
            )
        }else{
            sender.sendMessage(
                "<red>You must be the owner of the group to transfer ownership.".formatMinimessage()
            )
        }
    }

}