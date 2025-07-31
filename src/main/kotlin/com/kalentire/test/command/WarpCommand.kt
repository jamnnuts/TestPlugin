package com.kalentire.test.command

import com.ravingarinc.api.command.BaseCommand
import com.ravingarinc.api.module.RavinPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.function.BiFunction

class WarpCommand(plugin: RavinPlugin) : BaseCommand(plugin, "warp", null, "/warp <x> <y> <z> <player> | Warps player to location. If no player is provided, assumes command caller as default.", 0, BiFunction() { sender, args ->
    if (args.size > 4) {
        sender.sendMessage("Invalid arguments. Please follow /warp <location> <player>")
        return@BiFunction false
    }
    val x = args[0].toDoubleOrNull()
    val y = args[1].toDoubleOrNull()
    val z = args[2].toDoubleOrNull()

    if (x != null || y != null || z != null) {
        if (args.size == 3) {
            Bukkit.getPlayer(sender.name)?.teleportAsync(Location(Bukkit.getWorlds()[0], x!!, y!!, z!!))
        } else {
            val destPlayer = Bukkit.getServer().getPlayer(args[3])

            if (destPlayer == null) {
                sender.sendMessage("Player does not exist!")
                return@BiFunction false
            }

            Bukkit.getPlayer(sender.name)?.teleportAsync(destPlayer.location)
        }
    } else {
        sender.sendMessage("Coordinates are invalid.")
        return@BiFunction false
    }

    return@BiFunction true
})