package com.kalentire.test.persistent

import com.ravingarinc.api.module.RavinPlugin
import com.ravingarinc.api.module.warn
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

class ConfigFile(private val plugin: RavinPlugin, private val name: String) {
    private val file: File = File(plugin.dataFolder, name)
    val config: FileConfiguration get() = innerConfig
    private var innerConfig: FileConfiguration = YamlConfiguration.loadConfiguration(file)

    init {
        load()
    }

    fun load() {
        plugin.getResource(name)?.let {
            it.use { stream ->
                config.setDefaults(YamlConfiguration.loadConfiguration(InputStreamReader(stream)))
            }
        }
        config.options().copyDefaults(true)
        save()
    }

    fun reload() {
        innerConfig = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        config.save(file)
    }

    fun <T> read(path: String, default: T, section: ConfigurationSection.(String) -> T?): T {
        val result = section.invoke(config, path)
        if (result == null) {
            warn("Could not find option at '$path' in ${config.name}!")
            return default
        }
        return result
    }

    fun <T> read(path: String, section: ConfigurationSection.(String) -> T?): T? {
        return read(path, null, section)
    }

    fun consume(path: String, consumer: (ConfigurationSection) -> Unit) {
        config.getConfigurationSection(path)?.let {
            consumer.invoke(it)
        }
    }
}