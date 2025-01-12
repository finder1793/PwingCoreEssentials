package com.pwing.essentials.gui;

import com.pwing.essentials.EssentialsPlugin;
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

public class WarpMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final List<String> categories;
    private final Map<String, Map<String, Location>> warps;
    private int currentPage;
    private String currentCategory;

    public WarpMenu(EssentialsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.warps = plugin.getWarpManager().getWarps();
        this.categories = new ArrayList<>(warps.keySet());
        this.currentPage = 0;
        this.currentCategory = null;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Select a Warp - Page " + (currentPage + 1));
        initializeItems(inventory);
        player.openInventory(inventory);
    }

    private void initializeItems(Inventory inventory) {
        if (currentCategory == null) {
            // Display categories
            int startIndex = currentPage * 45;
            int endIndex = Math.min(startIndex + 45, categories.size());

            for (int i = startIndex; i < endIndex; i++) {
                String category = categories.get(i);
                ItemStack item = new ItemStack(Material.BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(category);
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

            if (endIndex < categories.size()) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = nextPage.getItemMeta();
                nextMeta.setDisplayName("Next Page");
                nextPage.setItemMeta(nextMeta);
                inventory.setItem(53, nextPage);
            }
        } else {
            // Display warps in the current category
            List<String> warpNames = new ArrayList<>(warps.get(currentCategory).keySet());
            int startIndex = currentPage * 45;
            int endIndex = Math.min(startIndex + 45, warpNames.size());

            for (int i = startIndex; i < endIndex; i++) {
                String warpName = warpNames.get(i);
                ItemStack item = new ItemStack(Material.ENDER_PEARL);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(warpName);
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

            if (endIndex < warpNames.size()) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = nextPage.getItemMeta();
                nextMeta.setDisplayName("Next Page");
                nextPage.setItemMeta(nextMeta);
                inventory.setItem(53, nextPage);
            }

            // Add back button
            ItemStack backButton = new ItemStack(Material.BARRIER);
            ItemMeta backMeta = backButton.getItemMeta();
            backMeta.setDisplayName("Back to Categories");
            backButton.setItemMeta(backMeta);
            inventory.setItem(49, backButton);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Select a Warp")) { // Use getView().getTitle() directly
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.getType() == Material.BOOK) {
                    // Category selected
                    currentCategory = clickedItem.getItemMeta().getDisplayName();
                    currentPage = 0;
                    open();
                } else if (clickedItem.getType() == Material.ENDER_PEARL) {
                    // Warp selected
                    String warpName = clickedItem.getItemMeta().getDisplayName();
                    Map<String, Location> categoryWarps = warps.get(currentCategory);
                    if (categoryWarps.containsKey(warpName)) {
                        player.teleport(categoryWarps.get(warpName));
                        player.sendMessage("Teleporting to warp: " + warpName);
                        player.closeInventory();
                    }
                } else if (clickedItem.getType() == Material.ARROW) {
                    // Navigation buttons
                    String displayName = clickedItem.getItemMeta().getDisplayName();
                    if (displayName.equals("Previous Page")) {
                        currentPage--;
                        open();
                    } else if (displayName.equals("Next Page")) {
                        currentPage++;
                        open();
                    }
                } else if (clickedItem.getType() == Material.BARRIER) {
                    // Back to categories
                    currentCategory = null;
                    currentPage = 0;
                    open();
                }
            }
        }
    }
}
