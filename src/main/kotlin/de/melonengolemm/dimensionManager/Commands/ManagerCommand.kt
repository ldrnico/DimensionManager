package de.melonengolemm.dimensionManager.Commands

import de.melonengolemm.dimensionManager.DimensionManager
import de.melonengolemm.dimensionManager.Handlers.DimensionHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ManagerCommand(
    private val plugin: DimensionManager,
    private val dimensionHandler: DimensionHandler
) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("dimensionmanager.command")) {
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
        if (!sender.hasPermission("dimensionmanager.config.reload")) {
            sender.sendMessage("§cYou do not have permission to reload the config.")
            return
        }

        if (args.size < 2 || args[1].lowercase() != "reload") {
            sender.sendMessage("§cUsage: /dm config reload")
            return
        }

        plugin.reloadConfig()
        dimensionHandler.ensureDefaultConfig()
        sender.sendMessage("§aConfig reloaded from config.yml.")
    }

    private fun handleDimensionCommand(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("dimensionmanager.dimension.manage")) {
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
                dimensionHandler.setDimensionStatus(dimension, "open")
                sender.sendMessage("§a${dimension.replaceFirstChar { it.titlecase() }} is now open.")
            }
            "close" -> {
                dimensionHandler.setDimensionStatus(dimension, "closed")
                sender.sendMessage("§c${dimension.replaceFirstChar { it.titlecase() }} is now closed.")
            }
            "info" -> {
                val status = dimensionHandler.getDimensionStatus(dimension)
                sender.sendMessage("§7${dimension.replaceFirstChar { it.titlecase() }} status: $status")
            }
            else -> {
                sender.sendMessage("§cInvalid action. Valid actions are: open, close, info.")
            }
        }
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage("§cUsage: /dm config reload | /dm <overworld|nether|end> <open|close|info>")
    }
}