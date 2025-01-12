package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.gui.WarpMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final EssentialsPlugin plugin;

    public WarpCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.warp")) {
                new WarpMenu(plugin, player).open();
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
