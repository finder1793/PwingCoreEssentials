package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.kits.KitEditor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitEditorCommand implements CommandExecutor {

    private EssentialsPlugin plugin;

    public KitEditorCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentials.kit.edit")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /kiteditor <kitname>");
            return true;
        }

        String kitName = args[0];
        KitEditor.openKitEditor(player, kitName, plugin);

        return true;
    }
}
