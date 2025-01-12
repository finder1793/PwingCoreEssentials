package com.yourname.essentials.gui;

import com.yourname.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final List<String> homeNames;
    private int currentPage;

    public HomeMenu(EssentialsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.homeNames = new ArrayList<>(plugin.getHomeManager().getHomes(player).keySet());
        this.currentPage = 0;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Select a Home - Page " + (currentPage + 1));
        initializeItems(inventory);
        player.openInventory(inventory);
    }

    private void initializeItems(Inventory inventory) {
        int startIndex = currentPage * 45;
        int endIndex = Math.min(startIndex + 45, homeNames.size());

        for (int i = startIndex; i < endIndex; i++) {
            String homeName = homeNames.get(i);
            ItemStack item = new ItemStack(Material.RED_BED); // Replace Material.BED with Material.RED_BED
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(homeName);
            item.setItemMeta(meta);
            inventory.setItem(i - startIndex, item);
        }

        // Add navigation buttons
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName("Previous Page");
            prevPage.setItemMeta(prevMeta);
            inventory.setItem(45, prevPage);
        }

        if (endIndex < homeNames.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("Next Page");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Select a Home")) { // Use getView().getTitle() directly
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.getType() == Material.RED_BED) { // Replace Material.BED with Material.RED_BED
                    String homeName = clickedItem.getItemMeta().getDisplayName();
                    Map<String, Location> homes = plugin.getHomeManager().getHomes(player);
                    if (homes.containsKey(homeName)) {
                        player.teleport(homes.get(homeName));
                        player.sendMessage("Teleporting to home: " + homeName);
                        player.closeInventory();
                    }
                } else if (clickedItem.getType() == Material.ARROW) {
                    String displayName = clickedItem.getItemMeta().getDisplayName();
                    if (displayName.equals("Previous Page")) {
                        currentPage--;
                        open();
                    } else if (displayName.equals("Next Page")) {
                        currentPage++;
                        open();
                    }
                }
            }
        }
    }
}
