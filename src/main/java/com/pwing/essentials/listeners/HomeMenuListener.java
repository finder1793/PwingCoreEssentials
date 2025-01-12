package com.pwing.essentials.listeners;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.gui.HomeMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;

public class HomeMenuListener implements Listener {

    private final EssentialsPlugin plugin;

    public HomeMenuListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) { // Check if the clicker is a Player
            Player player = (Player) event.getWhoClicked(); // Cast to Player
            new HomeMenu(plugin, player).onInventoryClick(event);
        }
    }
}