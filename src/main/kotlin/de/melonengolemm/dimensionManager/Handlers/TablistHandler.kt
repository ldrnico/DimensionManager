package de.melonengolemm.dimensionManager.Handlers

import de.melonengolemm.dimensionManager.DimensionManager
import org.bukkit.plugin.java.JavaPlugin

class TablistHandler(val plugin: JavaPlugin) {

    private val configHandler: ConfigHandler
        get() = (plugin as? DimensionManager)?.configHandler ?: ConfigHandler(plugin)

    fun TabListTask() {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            if (!configHandler.readConfigBoolean("tablist.enabled", false)) {
                for (player in plugin.server.onlinePlayers) {
                    player.setPlayerListHeaderFooter("", "")
                }
                return@Runnable
            }

            val players = plugin.server.onlinePlayers
            if (players.isEmpty()) return@Runnable

            val netherStatus = if (configHandler.readConfigBoolean("dimensions.nether.is-open", true)) {
                configHandler.readConfigString("tablist.nether.open", "§aOpen")
            } else {
                configHandler.readConfigString("tablist.nether.closed", "§cClosed")
            }

            val overworldStatus = if (configHandler.readConfigBoolean("dimensions.overworld.is-open", true)) {
                configHandler.readConfigString("tablist.overworld.open", "§aOpen")
            } else {
                configHandler.readConfigString("tablist.overworld.closed", "§cClosed")
            }

            val endStatus = if (configHandler.readConfigBoolean("dimensions.end.is-open", true)) {
                configHandler.readConfigString("tablist.end.open", "§aOpen")
            } else {
                configHandler.readConfigString("tablist.end.closed", "§cClosed")
            }

            val footerTemplate = configHandler.readConfigString("tablist.format")
            val footer = footerTemplate
                .replace("\$overworld", overworldStatus)
                .replace("\$nether", netherStatus)
                .replace("\$end", endStatus)
                .replace('&', '§')

            for (player in players) {
                player.setPlayerListHeaderFooter("", footer)
            }
        }, 0L, 20L)
    }
}
