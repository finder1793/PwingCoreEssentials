package com.yourname.essentials.commands;

import com.yourname.essentials.EssentialsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {

    private final EssentialsPlugin plugin;

    public DelWarpCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.delwarp")) {
                if (args.length > 1) {
                    String category = args[0];
                    String warpName = args[1];
                    plugin.getWarpManager().deleteWarp(category, warpName);
                    player.sendMessage("Warp deleted: " + category + " - " + warpName);
                    return true;
                } else {
                    player.sendMessage("Usage: /delwarp <category> <name>");
                    return false;
                }
            } else {
                player.sendMessage("You do not have permission to use this command.");
                return false;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}
