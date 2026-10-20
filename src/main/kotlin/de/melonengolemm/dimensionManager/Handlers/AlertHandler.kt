package de.melonengolemm.dimensionManager.Handlers

import de.melonengolemm.dimensionManager.DimensionManager
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.plugin.java.JavaPlugin

class AlertHandler(public val plugin: JavaPlugin) {

    private val configHandler: ConfigHandler
        get() {
            return (plugin as? DimensionManager)?.configHandler ?: ConfigHandler(plugin)
        }

    fun sendMessageAlert(dimension: String, action: String) {
        if (!configHandler.readConfigBoolean("alerts.enabled", true)) {
            return
        }

        val dim = dimension.lowercase()
        val act = action.lowercase()

        val prefix = configHandler.readConfigString("alerts.format.prefix").ifEmpty {
            configHandler.readConfigString("alerts.prefix")
        }

        // Try format.<dim>-<action> (e.g. nether-open, nether-close)
        var message = configHandler.readConfigString("alerts.format.$dim-$act")

        // Fallback for close vs closed
        if (message.isEmpty()) {
            val altAct = if (act == "close") "closed" else if (act == "closed") "close" else act
            message = configHandler.readConfigString("alerts.format.$dim-$altAct")
        }

        // Fallback without format section
        if (message.isEmpty()) {
            message = configHandler.readConfigString("alerts.$dim-$act")
        }

        if (message.isNotEmpty()) {
            val fullMessage = "$prefix$message".replace('&', '§')
            Bukkit.broadcastMessage(fullMessage)
        }
    }

    fun sendSoundAlert(dimension: String, action: String) {
        if (!configHandler.readConfigBoolean("sound.enabled", true)) {
            return
        }

        val dim = dimension.lowercase()
        val act = action.lowercase()

        // Try sound.format.<dim>-<action> (e.g. nether-open, nether-close)
        var soundName = configHandler.readConfigString("sound.format.$dim-$act")

        // Fallback for close vs closed
        if (soundName.isEmpty()) {
            val altAct = if (act == "close") "closed" else if (act == "closed") "close" else act
            soundName = configHandler.readConfigString("sound.format.$dim-$altAct")
        }

        // Fallback without format section
        if (soundName.isEmpty()) {
            soundName = configHandler.readConfigString("sound.$dim-$act")
        }

        if (soundName.isBlank()) {
            return
        }

        // Try Bukkit Sound enum (e.g. ENTITY_ENDER_DRAGON_GROWL)
        val sound: Sound? = try {
            Sound.valueOf(soundName.trim().uppercase())
        } catch (_: IllegalArgumentException) {
            null
        }

        val volume = configHandler.readConfigFloat("sound.volume", 0.1f)

        for (player in Bukkit.getOnlinePlayers()) {
            if (sound != null) {
                player.playSound(player.location, sound, volume, 1.0f)
            } else {
                // Fallback for custom / namespaced sound keys
                player.playSound(player.location, soundName.trim().lowercase(), volume, 1.0f)
            }
        }
    }
}
