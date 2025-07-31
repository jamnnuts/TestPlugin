package com.kalentire.test.command

import com.charleskorn.kaml.Yaml
import com.kalentire.test.util.SavedWarp
import com.ravingarinc.api.command.BaseCommand
import com.ravingarinc.api.module.RavinPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import java.nio.file.Path
import java.util.function.BiFunction

class WarpsCommand(plugin: RavinPlugin) : BaseCommand(plugin, "warps", null, "/warps | Shows list of warps", 0, BiFunction() { sender, args ->
    if (args.size > 1) {
        sender.sendMessage("Invalid arguments. Please follow /warps")
        return@BiFunction false
    }

    val player = Bukkit.getPlayer(sender.name)
    if (player == null) {
        sender.sendMessage("Internal Server error. Player does not exist.")
        return@BiFunction false
    }

    try {
        var warpString = "Warps:"
        Path.of("${player.name}_warps.yaml").toFile().readText().split("---").map{
            Yaml.default.decodeFromString(SavedWarp.serializer(), it)
        }.forEach {
            warpString.plus(it.toString() + "\n")
        }

        sender.sendMessage(warpString)
    } catch (ex: Exception) {
        sender.sendMessage("Internal server error occurred.")
        return@BiFunction false
    }

    return@BiFunction true
})