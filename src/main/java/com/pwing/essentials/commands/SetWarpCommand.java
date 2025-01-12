package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private final EssentialsPlugin plugin;

    public SetWarpCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.setwarp")) {
                if (args.length > 1) {
                    String category = args[0];
                    String warpName = args[1];
                    Location location = player.getLocation();
                    plugin.getWarpManager().setWarp(category, warpName, location);
                    player.sendMessage("Warp set: " + category + " - " + warpName);
                    return true;
                } else {
                    player.sendMessage("Usage: /setwarp <category> <name>");
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
