package de.melonengolemm.dimensionManager.Commands

import de.melonengolemm.dimensionManager.DimensionManager
import de.melonengolemm.dimensionManager.Handlers.AlertHandler
import de.melonengolemm.dimensionManager.Handlers.ConfigHandler
import de.melonengolemm.dimensionManager.Handlers.DimensionHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ManagerCommand(
    private val plugin: DimensionManager,
    private val dimensionHandler: DimensionHandler,
    private val configHandler: ConfigHandler,
    private val alertHandler: AlertHandler
) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("dimensionmanager.admin") && !sender.hasPermission("dimensionmanager.command")) {
            sender.sendMessage("§cYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        when (args[0].lowercase()) {
            "config" -> handleConfigCommand(sender, args)
            "overworld", "nether", "end" -> handleDimensionCommand(sender, args)
            else -> {
                sendUsage(sender)
                return true
            }
        }
        return true
    }

    private fun handleConfigCommand(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("dimensionmanager.admin") &&
            !sender.hasPermission("dimensionmanager.command") &&
            !sender.hasPermission("dimensionmanager.config.reload")
        ) {
            sender.sendMessage("§cYou do not have permission to view or reload the config.")
            return
        }

        // /dm config without reload shows the formatted overview
        if (args.size == 1 || (args.size >= 2 && (args[1].equals("info", ignoreCase = true) || args[1].equals("show", ignoreCase = true)))) {
            configHandler.sendFormattedConfig(sender)
            return
        }

        if (args[1].lowercase() == "reload") {
            if (!sender.hasPermission("dimensionmanager.config.reload") && !sender.hasPermission("dimensionmanager.admin")) {
                sender.sendMessage("§cYou do not have permission to reload the config.")
                return
            }

            plugin.reloadConfig()
            dimensionHandler.ensureDefaultConfig()
            sender.sendMessage("§aConfig reloaded from config.yml.")
            return
        }

        sender.sendMessage("§cUsage: /dm config [reload]")
    }

    private fun handleDimensionCommand(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("dimensionmanager.dimension.manage") && !sender.hasPermission("dimensionmanager.admin")) {
            sender.sendMessage("§cYou do not have permission to manage dimensions.")
            return
        }

        if (args.size != 2) {
            sender.sendMessage("§cUsage: /dm <overworld|nether|end> <open|close|info>")
            return
        }

        val dimension = args[0].lowercase()
        val action = args[1].lowercase()

        when (action) {
            "open" -> {
                if (configHandler.readConfigBoolean("dimensions.$dimension.is-open", true)) {
                    sender.sendMessage("§c${dimension.replaceFirstChar { it.titlecase() }} is already open.")
                    return
                }
                configHandler.writeConfigBoolean("dimensions.$dimension.is-open", true)
                alertHandler.sendMessageAlert(dimension, action)
                alertHandler.sendSoundAlert(dimension, action)
            }
            "close" -> {
                if (!configHandler.readConfigBoolean("dimensions.$dimension.is-open", true)) {
                    sender.sendMessage("§c${dimension.replaceFirstChar { it.titlecase() }} is already closed.")
                    return
                }
                configHandler.writeConfigBoolean("dimensions.$dimension.is-open", false)
                alertHandler.sendMessageAlert(dimension, action)
                alertHandler.sendSoundAlert(dimension, action)
            }
            "info" -> {
                val status = configHandler.readConfigBoolean("dimensions.$dimension.is-open", true)
                val statusDisplay = if (status) "§aOpen" else "§cClosed"
                sender.sendMessage("§7${dimension.replaceFirstChar { it.titlecase() }} status: $statusDisplay")
            }
            else -> {
                sender.sendMessage("§cInvalid action. Valid actions are: open, close, info.")
            }
        }
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage("§cUsage: /dm config [reload] | /dm <overworld|nether|end> <open|close|info>")
    }
}
