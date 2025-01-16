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

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatModifier;
import net.minecraft.server.v1_16_R3.ChatClickable;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;

import com.github.retrooper.packetevents.protocol.packetwrapper.play.server.WrapperPlayServerPlayerInfo;

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
                Hologram hologramToEdit = holograms.get(name);
                player.sendMessage("Editing hologram: " + name);
                for (int i = 0; i < hologramToEdit.getLines().size(); i++) {
                    String line = hologramToEdit.getLines().get(i);
                    IChatBaseComponent message = new ChatComponentText("Line " + (i + 1) + ": " + line);
                    IChatBaseComponent editButton = new ChatComponentText(ChatColor.GREEN + "[Edit]");
                    editButton.setChatModifier(new ChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/hologram editline " + name + " " + i + " ")));
                    IChatBaseComponent deleteButton = new ChatComponentText(ChatColor.RED + "[Delete]");
                    deleteButton.setChatModifier(new ChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.RUN_COMMAND, "/hologram deleteline " + name + " " + i)));
                    message.addSibling(new ChatComponentText(" "));
                    message.addSibling(editButton);
                    message.addSibling(new ChatComponentText(" "));
                    message.addSibling(deleteButton);
                    PacketPlayOutChat packet = new PacketPlayOutChat(message);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
                break;
            case "editline":
                if (name == null || args.length < 4 || !holograms.containsKey(name)) {
                    player.sendMessage("Usage: /hologram editline <name> <index> <new line>");
                    return true;
                }
                int indexToEdit;
                try {
                    indexToEdit = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid index.");
                    return true;
                }
                String newLine = String.join(" ", args, 3, args.length);
                holograms.get(name).setLine(indexToEdit, newLine);
                player.sendMessage("Hologram line updated.");
                break;
            case "deleteline":
                if (name == null || args.length < 3 || !holograms.containsKey(name)) {
                    player.sendMessage("Usage: /hologram deleteline <name> <index>");
                    return true;
                }
                int indexToDelete;
                try {
                    indexToDelete = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid index.");
                    return true;
                }
                holograms.get(name).removeLine(indexToDelete);
                player.sendMessage("Hologram line deleted.");
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
