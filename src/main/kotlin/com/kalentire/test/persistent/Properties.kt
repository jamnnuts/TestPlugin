package com.kalentire.test.persistent

import com.ravingarinc.api.module.RavinPlugin
import com.ravingarinc.api.module.SuspendingModule
import org.bukkit.configuration.ConfigurationSection

class Properties(plugin: RavinPlugin) : SuspendingModule(Properties::class.java, plugin) {
    val config: ConfigFile = ConfigFile(plugin, "config.yml")

    // if any of these are -1, then they should be ignored
    var randomOption = 3

    override suspend fun suspendLoad() {
        // See here for which values you can read.
        // YAML values read a '.' character as if it's a sub section of the parent, see the config.yml to see what I mean
        randomOption = config.read("options.random", 3, ConfigurationSection::getInt)
    }

    override suspend fun suspendCancel() {
        config.reload()
    }
}
