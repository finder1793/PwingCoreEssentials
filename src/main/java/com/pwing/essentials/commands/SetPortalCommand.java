package com.pwing.essentials.commands;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.gui.PortalEffectMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SetPortalCommand implements CommandExecutor, Listener {

    private final EssentialsPlugin plugin;
    private final Map<Player, Location> portalLocations = new HashMap<>();
    private final Map<Player, String> portalNames = new HashMap<>();

    public SetPortalCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.setportal")) {
                if (args.length > 0) {
                    String portalName = args[0];
                    ItemStack wand = new ItemStack(Material.BLAZE_ROD);
                    player.getInventory().addItem(wand);
                    player.sendMessage("Right-click with the wand to set the portal location.");
                    portalNames.put(player, portalName);
                    portalLocations.put(player, null);
                    return true;
                } else {
                    player.sendMessage("Usage: /setportal <name>");
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (portalLocations.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
            Location location = event.getClickedBlock().getLocation();
            if (portalLocations.get(player) == null) {
                portalLocations.put(player, location);
                player.sendMessage("Portal location set. Now right-click to set the destination.");
            } else {
                Location destination = location;
                String portalName = portalNames.get(player);
                portalLocations.remove(player);
                portalNames.remove(player);
                player.sendMessage("Portal " + portalName + " set from " + portalLocations.get(player) + " to " + destination);
                new PortalEffectMenu(plugin, player, portalName, portalLocations.get(player), destination).open();
            }
        }
    }
}
