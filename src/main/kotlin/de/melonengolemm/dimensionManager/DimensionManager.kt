package de.melonengolemm.dimensionManager

import de.melonengolemm.dimensionManager.Commands.ManagerCommand
import de.melonengolemm.dimensionManager.Handlers.DimensionHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DimensionManager : JavaPlugin(), Listener {
    lateinit var dimensionHandler: DimensionHandler

    override fun onEnable() {
        println("DimensionManager by melonengolemm enabled!")

        saveDefaultConfig()
        reloadConfig()

        dimensionHandler = DimensionHandler(this)
        dimensionHandler.ensureDefaultConfig()

        getCommand("dimensionmanager")?.setExecutor(ManagerCommand(this, dimensionHandler))
    }

    override fun onDisable() {
    }
}
