package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramCommand implements CommandExecutor {

    private final EssentialsPlugin plugin;
    private final Map<String, Hologram> holograms = new HashMap<>();

    public HologramCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Usage: /hologram <create|edit|remove> <name> [lines...]");
            return true;
        }

        String action = args[0];
        String name = args.length > 1 ? args[1] : null;

        switch (action.toLowerCase()) {
            case "create":
                if (name == null || args.length < 3) {
                    player.sendMessage("Usage: /hologram create <name> <lines...>");
                    return true;
                }
                List<String> lines = new ArrayList<>();
                for (int i = 2; i < args.length; i++) {
                    lines.add(args[i]);
                }
                Location location = player.getLocation();
                Hologram hologram = new Hologram(location, lines);
                holograms.put(name, hologram);
                hologram.display(player);
                player.sendMessage("Hologram created.");
                break;
            case "edit":
                if (name == null || !holograms.containsKey(name)) {
                    player.sendMessage("Hologram not found.");
                    return true;
                }
                // Implement edit logic
                break;
            case "remove":
                if (name == null || !holograms.containsKey(name)) {
                    player.sendMessage("Hologram not found.");
                    return true;
                }
                holograms.get(name).hide(player);
                holograms.remove(name);
                player.sendMessage("Hologram removed.");
                break;
            default:
                player.sendMessage("Unknown action.");
                break;
        }

        return true;
    }
}
