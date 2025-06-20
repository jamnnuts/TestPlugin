package com.kalentire.test.command

import com.kalentire.test.module.TestModule
import com.ravingarinc.api.command.BaseCommand
import com.ravingarinc.api.module.RavinPlugin
import com.ravingarinc.api.module.getModule
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Basic default command.
 *
 * First argument is the plugin.
 *
 * Second argument is the prefix (so to use the command would mean executing /test)
 *
 * Third argument is the permission that a player must have to use this command. Otherwise set to null
 * if you don't want to require a permission.
 */
class TestCommand(plugin: RavinPlugin) : BaseCommand(plugin, "test", null) {
    val module = plugin.getModule<TestModule>()

    init {
        // this adds a subcommand called reload, which is executed with /test reload
        // first argument is the prefix, second arg
        // the '1' means that this command requires 1 extra argument total
        // aka /test is
        addOption("reload", "testplugin.reload", "This command reloads the plugin!", 1) { sender, args ->
            // sender is either a player or the server console

            if(sender is Player) {
                // do player things.
            }
            plugin.reload()
            sender.sendMessage("Plugin reloaded successfully!")
            return@addOption true // return true if command was successful
        }

        val possibleOptions = buildList {
            add("a")
            add("b")
            add("c")
            add("d")
        }

        // Null means that no permission is required
        // the '2' means that this command requires 2 extra
        addOption("example", null, "<option> - This is an example description.", 2) { sender, args ->

            /*
            I won't lie, I really hate how I've made this API, because of how the args and indices work
            with the array.

            In this case the values in the args array will be as follows
            args[0] = "test"
            args[1] = "example"
            args[2] = "<option>" (or rather whatever extra argument the player input here.
             */
            val option = args[2] // do somethign with argument. The values in this array will always be strings
            sender.sendMessage("You entered the argument ${option}")

            // You can find docs on this cool component text here
            // https://docs.papermc.io/paper/dev/component-api/introduction/
            sender.sendMessage(Component.text("You can also do cool component text like this! ")
                .color(NamedTextColor.RED)
                .append(
                    Component.text("[Click Me]").clickEvent(ClickEvent.callback { audience ->
                        // execute code for the given audience
                        // audiences are paper's way of handling a receiver of a message.
                        // you can get an optional uuid (and therefore get the player object itself like so)
                        // read more here! https://docs.papermc.io/paper/dev/component-api/audiences/

                        audience.get(Identity.UUID).ifPresent {
                            Bukkit.getPlayer(it)?.let { player ->
                                player.sendMessage("You just clicked me!")
                            }
                        }
                    })))

            return@addOption true // return true if command was successful
        }.buildTabCompletions { sender, args ->
            // this is where you can specify the autofill feature. So depending on what argument the player is at.
            // returning null will by default list the name of all online players
            if(args.size == 3) {
                // if player is currently entering something in <option> part of the command
                return@buildTabCompletions possibleOptions
            }
            return@buildTabCompletions emptyList() // this will show no suggestions
        }


        addHelpOption(ChatColor.RED, ChatColor.DARK_RED) // Auto add a help option.
    }
}