package de.melonengolemm.dimensionManager.Handlers

import org.bukkit.plugin.java.JavaPlugin

class DimensionHandler(private val plugin: JavaPlugin) {

    fun ensureDefaultConfig() {
        val defaults = mapOf(
            "overworld" to "open",
            "nether" to "open",
            "end" to "open"
        )

        defaults.forEach { (dimension, status) ->
            val current = plugin.config.getString("dimensions.$dimension.status")
            if (current == null) {
                plugin.config.set("dimensions.$dimension.status", status)
            }
        }

        plugin.saveConfig()
    }

    fun getDimensionStatus(dim: String): String {
        val normalized = dim.lowercase()
        return plugin.config.getString("dimensions.$normalized.status", "unknown").toString()
    }

    fun setDimensionStatus(dim: String, status: String) {
        val normalized = dim.lowercase()
        plugin.config.set("dimensions.$normalized.status", status.lowercase())
        plugin.saveConfig()
    }
}