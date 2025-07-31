package com.kalentire.test.command

import com.charleskorn.kaml.Yaml
import com.kalentire.test.util.SavedWarp
import com.ravingarinc.api.command.BaseCommand
import com.ravingarinc.api.module.RavinPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.function.BiFunction

class DeleteWarpCommand(plugin: RavinPlugin) : BaseCommand(plugin, "delwarp", null, "/delwarp <Warp Name> | Deletes warp with the provided name", 0, BiFunction() { sender, args ->
    if (args.size > 2) {
        sender.sendMessage("Invalid arguments. Please follow /delwarp <Warp Name>")
    } else {
        val player = Bukkit.getPlayer(sender.name)
        if (player == null) {
            sender.sendMessage("Internal server error. Player does not exist.")
        } else {
            try {
                var warpList = emptyList<SavedWarp>()

                val temp = Path.of("${player.name}_warps.yaml").toFile().readText().split("---").map{
                    Yaml.default.decodeFromString(SavedWarp.serializer(), it)
                }
                temp.forEach {
                    if (args[1] == it.name) {
                        warpList = temp.drop(1)
                    }
                }

                if (warpList.isNotEmpty()) {
                    FileOutputStream("${player.name}_warps.yaml").bufferedWriter().use { writer -> {
                        warpList.forEach {
                            writer.write(Yaml.default.encodeToString(SavedWarp.serializer(), it))
                            writer.write("---")
                        }
                    } }
                    sender.sendMessage("Warp ${args[1]} deleted.")
                    return@BiFunction true
                }

                sender.sendMessage("Warp ${args[1]} does not exist.")
            } catch (e: Exception) {
                sender.sendMessage("Internal server error occurred.")
            }
        }
    }

    return@BiFunction false
})