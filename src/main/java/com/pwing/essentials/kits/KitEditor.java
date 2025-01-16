package com.pwing.essentials.kits;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitEditor implements Listener {

    private static final String KIT_EDITOR_TITLE = ChatColor.GREEN + "Kit Editor: ";

    private EssentialsPlugin plugin;
    private String kitName;
    private Inventory inventory;

    public KitEditor(EssentialsPlugin plugin, String kitName) {
        this.plugin = plugin;
        this.kitName = kitName;
        this.inventory = Bukkit.createInventory(null, 27, KIT_EDITOR_TITLE + kitName);
        loadKitItems();
    }

    public static void openKitEditor(Player player, String kitName, EssentialsPlugin plugin) {
        KitEditor kitEditor = new KitEditor(plugin, kitName);
        Bukkit.getPluginManager().registerEvents(kitEditor, plugin);
        player.openInventory(kitEditor.getInventory());
    }

    private void loadKitItems() {
        KitManager kitManager = plugin.getKitManager();
        if (kitManager.kitExists(kitName)) {
            Inventory kitInventory = kitManager.getKitInventory(kitName);
            inventory.setContents(kitInventory.getContents());
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(KIT_EDITOR_TITLE + kitName)) {
            // Allow editing the inventory
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(KIT_EDITOR_TITLE + kitName)) {
            Player player = (Player) event.getPlayer();
            KitManager kitManager = plugin.getKitManager();
            kitManager.saveKit(kitName, inventory);
            player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " has been saved.");
            InventoryClickEvent.getHandlerList().unregister(this);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
    }
}
