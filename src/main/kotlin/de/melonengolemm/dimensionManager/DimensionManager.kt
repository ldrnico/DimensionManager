package de.melonengolemm.dimensionManager

import de.melonengolemm.dimensionManager.Commands.ManagerCommand
import de.melonengolemm.dimensionManager.Commands.TabCompleter
import de.melonengolemm.dimensionManager.Events.PortalEvent
import de.melonengolemm.dimensionManager.Handlers.AlertHandler
import de.melonengolemm.dimensionManager.Handlers.ConfigHandler
import de.melonengolemm.dimensionManager.Handlers.DimensionHandler
import de.melonengolemm.dimensionManager.Handlers.TablistHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DimensionManager : JavaPlugin(), Listener {

    companion object {
        lateinit var instance: DimensionManager
            private set
    }

    lateinit var configHandler: ConfigHandler
    lateinit var dimensionHandler: DimensionHandler
    lateinit var tablistHandler: TablistHandler

    override fun onEnable() {
        instance = this
        println("DimensionManager by melonengolemm enabled!")

        saveDefaultConfig()
        reloadConfig()

        configHandler = ConfigHandler(this)

        dimensionHandler = DimensionHandler(this)
        dimensionHandler.ensureDefaultConfig()

        tablistHandler = TablistHandler(this)
        tablistHandler.TabListTask()

        server.pluginManager.registerEvents(PortalEvent(this), this)

        val command = getCommand("dimensionmanager")
        command?.setExecutor(ManagerCommand(this, dimensionHandler, configHandler, AlertHandler(this)))
        command?.setTabCompleter(TabCompleter())
    }

    override fun onDisable() {
    }
}
