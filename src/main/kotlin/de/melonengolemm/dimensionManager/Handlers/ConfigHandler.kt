package de.melonengolemm.dimensionManager.Handlers

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.charset.StandardCharsets

class ConfigHandler(private val plugin: JavaPlugin) {

    fun readConfigString(key: String, default: String = ""): String {
        return plugin.config.getString(key, default) ?: default
    }

    fun readConfigBoolean(key: String, default: Boolean = false): Boolean {
        return plugin.config.getBoolean(key, default)
    }

    fun readConfigDouble(key: String, default: Double = 0.0): Double {
        return if (plugin.config.contains(key)) plugin.config.getDouble(key, default) else default
    }

    fun readConfigFloat(key: String, default: Float = 0.1f): Float {
        return readConfigDouble(key, default.toDouble()).toFloat()
    }

    fun writeConfigString(key: String, value: String) {
        plugin.config.set(key, value)
        save()
    }

    fun writeConfigBoolean(key: String, value: Boolean) {
        plugin.config.set(key, value)
        save()
    }

    fun writeConfigDouble(key: String, value: Double) {
        plugin.config.set(key, value)
        save()
    }

    fun reload() {
        plugin.reloadConfig()
    }

    fun save() {
        plugin.saveConfig()
        formatConfigFile()
    }

    fun sendFormattedConfig(sender: CommandSender) {
        val netherOpen = readConfigBoolean("dimensions.nether.is-open", true)
        val overworldOpen = readConfigBoolean("dimensions.overworld.is-open", true)
        val endOpen = readConfigBoolean("dimensions.end.is-open", true)

        val tablistEnabled = readConfigBoolean("tablist.enabled", false)
        val rawTabFormat = readConfigString("tablist.format").replace("\r", "")
        val tablistFormat = rawTabFormat.replace("\n", " ").trim()
        val tabNetherOpen = readConfigString("tablist.nether.open", "§aOpen")
        val tabNetherClosed = readConfigString("tablist.nether.closed", "§cClosed")
        val tabOverworldOpen = readConfigString("tablist.overworld.open", "§aOpen")
        val tabOverworldClosed = readConfigString("tablist.overworld.closed", "§cClosed")
        val tabEndOpen = readConfigString("tablist.end.open", "§aOpen")
        val tabEndClosed = readConfigString("tablist.end.closed", "§cClosed")

        val alertsEnabled = readConfigBoolean("alerts.enabled", true)
        val alertPrefix = readConfigString("alerts.format.prefix").ifEmpty {
            readConfigString("alerts.prefix")
        }
        val endOpenMsg = readConfigString("alerts.format.end-open")
        val endCloseMsg = readConfigString("alerts.format.end-close")
        val netherOpenMsg = readConfigString("alerts.format.nether-open")
        val netherCloseMsg = readConfigString("alerts.format.nether-close")
        val overworldOpenMsg = readConfigString("alerts.format.overworld-open")
        val overworldCloseMsg = readConfigString("alerts.format.overworld-close")

        val soundEnabled = readConfigBoolean("sound.enabled", true)
        val soundVolume = readConfigFloat("sound.volume", 0.1f)
        val endOpenSound = readConfigString("sound.format.end-open")
        val endCloseSound = readConfigString("sound.format.end-close")
        val netherOpenSound = readConfigString("sound.format.nether-open")
        val netherCloseSound = readConfigString("sound.format.nether-close")
        val overworldOpenSound = readConfigString("sound.format.overworld-open")
        val overworldCloseSound = readConfigString("sound.format.overworld-close")

        fun state(isOpen: Boolean) = if (isOpen) "§aOpen" else "§cClosed"
        fun status(enabled: Boolean) = if (enabled) "§aEnabled" else "§cDisabled"

        sender.sendMessage("§7§m----------------§r §b[DimensionManager Config] §7§m----------------")
        sender.sendMessage("§6Dimensions:")
        sender.sendMessage("  §7• Overworld: ${state(overworldOpen)}")
        sender.sendMessage("  §7• Nether: ${state(netherOpen)}")
        sender.sendMessage("  §7• End: ${state(endOpen)}")
        sender.sendMessage("")
        sender.sendMessage("§6Tablist: §7(${status(tablistEnabled)}§7)")
        sender.sendMessage("  §7• Format: §f$tablistFormat")
        sender.sendMessage("  §7• Overworld: $tabOverworldOpen §7/ $tabOverworldClosed")
        sender.sendMessage("  §7• Nether: $tabNetherOpen §7/ $tabNetherClosed")
        sender.sendMessage("  §7• End: $tabEndOpen §7/ $tabEndClosed")
        sender.sendMessage("")
        sender.sendMessage("§6Alerts: §7(${status(alertsEnabled)}§7)")
        sender.sendMessage("  §7• Prefix: §r$alertPrefix")
        sender.sendMessage("  §7• Overworld: §aOpen: §r$overworldOpenMsg §7| §cClose: §r$overworldCloseMsg")
        sender.sendMessage("  §7• Nether: §aOpen: §r$netherOpenMsg §7| §cClose: §r$netherCloseMsg")
        sender.sendMessage("  §7• End: §aOpen: §r$endOpenMsg §7| §cClose: §r$endCloseMsg")
        sender.sendMessage("")
        sender.sendMessage("§6Sound: §7(${status(soundEnabled)}§7, Volume: §e$soundVolume§7)")
        sender.sendMessage("  §7• Overworld: §aOpen: §e$overworldOpenSound §7| §cClose: §e$overworldCloseSound")
        sender.sendMessage("  §7• Nether: §aOpen: §e$netherOpenSound §7| §cClose: §e$netherCloseSound")
        sender.sendMessage("  §7• End: §aOpen: §e$endOpenSound §7| §cClose: §e$endCloseSound")
        sender.sendMessage("§7§m-----------------------------------------------------")
    }

    private fun formatConfigFile() {
        try {
            val configFile = File(plugin.dataFolder, "config.yml")
            if (!configFile.exists()) return

            val lines = configFile.readLines(StandardCharsets.UTF_8)
            val sections = mutableMapOf<String, MutableList<String>>()
            var currentSection = ""

            for (line in lines) {
                if (!line.startsWith(" ") && !line.startsWith("\t") && line.contains(":") && !line.startsWith("#")) {
                    currentSection = line.substringBefore(":").trim()
                    sections[currentSection] = mutableListOf(line)
                } else if (currentSection.isNotEmpty()) {
                    sections[currentSection]?.add(line)
                }
            }

            // Desired order of top-level sections
            val order = listOf("dimensions", "tablist", "alerts", "sound")
            val orderedKeys = order.filter { sections.containsKey(it) } + sections.keys.filter { it !in order }

            val sb = StringBuilder()
            for ((index, key) in orderedKeys.withIndex()) {
                val secLines = sections[key] ?: continue
                while (secLines.isNotEmpty() && secLines.last().isBlank()) {
                    secLines.removeAt(secLines.size - 1)
                }

                if (index > 0) {
                    val prevKey = orderedKeys[index - 1]
                    if (prevKey == "dimensions") {
                        // 2 blank lines after dimensions
                        sb.append("\n\n\n")
                    } else {
                        // 1 blank line between other main points
                        sb.append("\n\n")
                    }
                }
                sb.append(secLines.joinToString("\n"))
            }
            sb.append("\n")

            configFile.writeText(sb.toString(), StandardCharsets.UTF_8)
        } catch (_: Exception) {
            // Ignore if formatting fails
        }
    }
}
