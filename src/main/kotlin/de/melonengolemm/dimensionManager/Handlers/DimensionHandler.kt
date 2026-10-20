package de.melonengolemm.dimensionManager.Handlers

import de.melonengolemm.dimensionManager.DimensionManager
import org.bukkit.plugin.java.JavaPlugin

class DimensionHandler(private val plugin: JavaPlugin) {

    private val configHandler: ConfigHandler
        get() = (plugin as? DimensionManager)?.configHandler ?: ConfigHandler(plugin)

    fun ensureDefaultConfig() {
        plugin.config.options().copyDefaults(true)
        configHandler.save()
    }
}
