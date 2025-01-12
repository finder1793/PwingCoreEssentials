package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private final EssentialsPlugin plugin;

    public SetHomeCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.sethome")) {
                String homeName = args.length > 0 ? args[0] : "default";
                Location location = player.getLocation();

                if (plugin.getHomeManager().canSetHome(player)) {
                    plugin.getHomeManager().setHome(player, homeName, location);
                    player.sendMessage("Home set: " + homeName);
                } else {
                    player.sendMessage("You cannot set more homes. Maximum allowed: " + plugin.getHomeManager().getMaxHomes(player));
                }
                return true;
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
