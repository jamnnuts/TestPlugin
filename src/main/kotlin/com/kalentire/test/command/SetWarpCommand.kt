package com.kalentire.test.command

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.encodeToStream
import com.kalentire.test.util.SavedWarp
import com.ravingarinc.api.command.BaseCommand
import com.ravingarinc.api.module.RavinPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.function.BiFunction

class SetWarpCommand(plugin: RavinPlugin) : BaseCommand(plugin, "setwarp", null, "/setwarp <Warp-Name> | Sets a warp point at the players location with the specified name.", 0, BiFunction() { sender, args ->
    if (args.size != 2) {
        sender.sendMessage("Usage: /setwarp <Warp-Name>")
        return@BiFunction false
    }

    val player = Bukkit.getPlayer(sender.name)
    if (player == null) {
        sender.sendMessage("Internal server error. Player does not exist.")
        return@BiFunction false
    }

    if (args[1].contains("---")) {
        sender.sendMessage("Warp name cannot contain ---") //XD
        return@BiFunction false
    }


    val savedWarp = SavedWarp(Bukkit.getWorlds()[0].name, player.x, player.y, player.z, player.yaw, player.pitch, args[1])
    //Write to yml
    try {
        Path.of("${player.name}_warps.yaml").toFile().readText().split("---").map{
            Yaml.default.decodeFromString(SavedWarp.serializer(), it)
        }.forEach {
            if (args[1] == it.name) {
                sender.sendMessage("Warp already exists! Please delete it using /delwarp first.")
                return@BiFunction false
            }
        }

        FileOutputStream("${player.name}_warps.yaml", true).bufferedWriter().use { writer -> {
            writer.write(Yaml.default.encodeToString(SavedWarp.serializer(), savedWarp))
            writer.write("---")
        } }

        sender.sendMessage("Warp Saved.")
    } catch (e: Exception) {
        if (e.cause is InvalidPathException) {
            try {
                FileOutputStream("${player.name}_warps.yaml", true).bufferedWriter().use { writer ->
                    {
                        writer.write(Yaml.default.encodeToString(SavedWarp.serializer(), savedWarp))
                        writer.write("---")
                    }
                }

                sender.sendMessage("Warp Saved.")
            } catch (e: Exception) {
                sender.sendMessage("Internal server error occurred. Warp could not be saved.")
                return@BiFunction false
            }
        } else {
            sender.sendMessage("Unknown Server Error.")
            return@BiFunction false
        }
    }


    return@BiFunction true
})