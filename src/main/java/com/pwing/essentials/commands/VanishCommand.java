package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private EssentialsPlugin plugin;

    public VanishCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("pwingcore.vanish")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (plugin.getVanishManager().isVanished(player)) {
            plugin.getVanishManager().setVanished(player, false);
            player.sendMessage(ChatColor.GREEN + "You are now visible.");
        } else {
            plugin.getVanishManager().setVanished(player, true);
            player.sendMessage(ChatColor.GREEN + "You are now vanished.");
        }

        return true;
    }
}
