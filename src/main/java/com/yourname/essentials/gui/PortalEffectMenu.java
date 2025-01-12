package com.yourname.essentials.gui;

import com.yourname.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalEffectMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final String portalName;
    private final Location portalLocation;
    private final Location portalDestination;

    public PortalEffectMenu(EssentialsPlugin plugin, Player player, String portalName, Location portalLocation, Location portalDestination) {
        this.plugin = plugin;
        this.player = player;
        this.portalName = portalName;
        this.portalLocation = portalLocation;
        this.portalDestination = portalDestination;
        this.inventory = Bukkit.createInventory(null, 54, "Select Portal Effect");

        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeItems();
    }

    public void open() {
        player.openInventory(inventory);
    }

    private void initializeItems() {
        int index = 0;
        for (Particle particle : Particle.values()) {
            if (index >= 54) break; // Limit to one page for simplicity
            ItemStack item = new ItemStack(Material.FIREWORK_STAR);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(particle.name());
            item.setItemMeta(meta);
            inventory.setItem(index++, item);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.FIREWORK_STAR) {
                String particleName = event.getCurrentItem().getItemMeta().getDisplayName();
                Particle particle = Particle.valueOf(particleName);
                player.sendMessage("Portal effect set to " + particleName);
                player.closeInventory();
                new ParticleAmountMenu(plugin, player, portalName, portalLocation, portalDestination, particle).open();
            }
        }
    }
}
