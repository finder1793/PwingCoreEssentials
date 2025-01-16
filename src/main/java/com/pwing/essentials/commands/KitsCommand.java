package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand implements CommandExecutor {

    private EssentialsPlugin plugin;
    private KitManager kitManager;

    public KitsCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.kitManager = plugin.getKitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /kits <kitname>");
            return true;
        }

        String kitName = args[0];

        if (!kitManager.kitExists(kitName)) {
            player.sendMessage(ChatColor.RED + "Kit " + kitName + " does not exist.");
            return true;
        }

        if (!player.hasPermission("essentials.kit." + kitName)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this kit.");
            return true;
        }

        if (kitManager.giveKit(player, kitName)) {
            player.sendMessage(ChatColor.GREEN + "You have received the " + kitName + " kit.");
        } else {
            player.sendMessage(ChatColor.RED + "Your inventory is too full to receive this kit.");
        }

        return true;
    }
}
