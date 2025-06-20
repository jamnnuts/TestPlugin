package com.kalentire.test.module

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.ravingarinc.api.module.RavinPlugin
import com.ravingarinc.api.module.SuspendingModule
import kotlinx.coroutines.Dispatchers
import org.bukkit.Bukkit

/**
 * All modules must only have ONE construction parameter, where that parameter is a RavinPlugin plugin. In this case
 * the TestPlugin extends RavinPlugin and will automagically be parsed in here.
 *
 * Specify the class identifier which is just the java class of this module. Is required means is this module required
 * for the plugin to work at all. You can also specify other modules that this module depends on, meaning this module
 * will not boot if it requires another module to work
 */
class TestModule(plugin: RavinPlugin) : SuspendingModule(TestModule::class.java, plugin, isRequired = true) {


    override suspend fun suspendLoad() {
        // This method is executed when the server boots up. If a plugin reload occurs, this method is called after suspendCancel().
        // Please design this with the expectation that the plugin might be reloaded.
        // If requiring a reload and you need to load some arbritrary data for a player, keep in mind that there may
        // be players on the server when a /reload command is used. Therefore in this method you may need to iterate
        // through every online player which can be gotten by Bukkit.getOnlinePlayers()
    }

    override suspend fun suspendCancel() {
        // Cancel should clear any caches, save any necessary data. This is executed when the server shuts down or if
        // a plugin reload occurs.
    }

    fun exampleJavaTaskCode() {
        // Below I will show the JAVA way of handling multiple tasks.
        // To execute a future task you can use the BukkitScheduler. Accessible like so
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            // execute a task that will execute after the given amount of ticks.

            // this task is executed on the main thread.
        }, 20L) // The long value here is in ticks, so 20 ticks per second. 1 tick = 50 milliseconds.

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            // this is a task which will be run asynchronously (completely off the main thread). You can call blocking
            // code here without freezing the main game.

            // remember though that you CANNOT utilise ANY Bukkit/Spigot/Paper API on an asynchronous thread otherwise
            // things will explode.
        })
    }

    fun exampleCoroutineTaskCode() {
        // The cool kotlin way however uses an external API
        // please see this quick little wiki if you wanna use these at all,
        // https://shynixn.github.io/MCCoroutine/wiki/site/coroutine/
        // To launch a coroutine on the context of the minecraft main server thread
        plugin.launch(plugin.minecraftDispatcher) {
            // suspendable code that when suspending will not block the main thread (i don't think so at least)
            // this code is executed on the main thread however.
        }

        // Launch a even cooler coroutine asynchronously
        plugin.launch(Dispatchers.IO) {
            // there also exists `plugin.asyncDispatcher` but due to funny coroutine things it's not recommended to use it,
            // Dispatchers.IO behaves the same way anyway, and by calling plugin.launch() we are always within the lifecycle
            // scope of the minecraft server itself.
            // do soemthing cool asynchronsouly wow no lag on server
        }
    }
}