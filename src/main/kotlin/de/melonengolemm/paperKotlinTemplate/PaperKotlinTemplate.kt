package de.melonengolemm.paperKotlinTemplate

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class PaperKotlinTemplate : JavaPlugin() {

    override fun onEnable() {
        println("${ChatColor.GREEN}Template enabled!")
    }

    override fun onDisable() {
    }
}
