package me.chaoticwagon.ecomc

import net.kyori.adventure.text.Component
import net .kyori.adventure.text.minimessage.MiniMessage

fun String.formatMinimessage(): Component {
    return MiniMessage.miniMessage().deserialize(this)
}