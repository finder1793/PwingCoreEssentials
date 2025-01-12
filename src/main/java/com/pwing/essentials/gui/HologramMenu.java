package com.pwing.essentials.gui;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class HologramMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final Map<String, Hologram> holograms;

    public HologramMenu(EssentialsPlugin plugin, Player player, Map<String, Hologram> holograms) {
        this.plugin = plugin;
        this.player = player;
        this.holograms = holograms;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Manage Holograms");
        initializeItems(inventory);
        player.openInventory(inventory);
    }

    private void initializeItems(Inventory inventory) {
        int index = 0;
        for (String name : holograms.keySet()) {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            inventory.setItem(index++, item);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Manage Holograms")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                ItemStack clickedItem = event.getCurrentItem();
                String name = clickedItem.getItemMeta().getDisplayName();
                // Implement click interactions
            }
        }
    }
}
