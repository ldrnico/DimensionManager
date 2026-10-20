package de.melonengolemm.dimensionManager.Events

import de.melonengolemm.dimensionManager.DimensionManager
import de.melonengolemm.dimensionManager.Handlers.ConfigHandler
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin

class PortalEvent(val plugin: JavaPlugin = DimensionManager.instance) : Listener {

    private val configHandler: ConfigHandler
        get() = (plugin as? DimensionManager)?.configHandler ?: ConfigHandler(plugin)

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerPortal(event: PlayerPortalEvent) {
        val fromWorld = event.from.world ?: return
        val targetDim = getTargetDimension(fromWorld, event.to, event.cause) ?: return

        if (!configHandler.readConfigBoolean("dimensions.$targetDim.is-open", true)) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onEntityPortal(event: EntityPortalEvent) {
        val fromWorld = event.from.world ?: return
        val targetDim = getTargetDimension(fromWorld, event.to, null) ?: return

        if (!configHandler.readConfigBoolean("dimensions.$targetDim.is-open", true)) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (event is PlayerPortalEvent) return

        val fromWorld = event.from.world ?: return
        val toWorld = event.to?.world ?: return

        if (fromWorld == toWorld) return

        val targetDim = getDimensionName(toWorld.environment) ?: return

        if (!configHandler.readConfigBoolean("dimensions.$targetDim.is-open", true)) {
            // Allow server admins with bypass to teleport across dimensions via commands
            if (event.player.hasPermission("dimensionmanager.admin") || event.player.hasPermission("dimensionmanager.bypass")) {
                return
            }
            event.isCancelled = true
        }
    }

    private fun getTargetDimension(
        fromWorld: World,
        toLocation: Location?,
        cause: PlayerTeleportEvent.TeleportCause?
    ): String? {
        val toWorld = toLocation?.world
        if (toWorld != null) {
            return getDimensionName(toWorld.environment) ?: toWorld.name.lowercase()
        }

        return when (cause) {
            PlayerTeleportEvent.TeleportCause.NETHER_PORTAL -> {
                if (fromWorld.environment == World.Environment.NETHER) "overworld" else "nether"
            }
            PlayerTeleportEvent.TeleportCause.END_PORTAL -> {
                if (fromWorld.environment == World.Environment.THE_END) "overworld" else "end"
            }
            PlayerTeleportEvent.TeleportCause.END_GATEWAY -> "end"
            else -> null
        }
    }

    private fun getDimensionName(environment: World.Environment): String? {
        return when (environment) {
            World.Environment.NORMAL -> "overworld"
            World.Environment.NETHER -> "nether"
            World.Environment.THE_END -> "end"
            else -> null
        }
    }
}

typealias PortalEvents = PortalEvent
