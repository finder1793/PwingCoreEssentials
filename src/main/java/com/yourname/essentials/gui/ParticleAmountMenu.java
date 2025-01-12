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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ParticleAmountMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final String portalName;
    private final Location portalLocation;
    private final Location portalDestination;
    private final Particle particle;

    public ParticleAmountMenu(EssentialsPlugin plugin, Player player, String portalName, Location portalLocation, Location portalDestination, Particle particle) {
        this.plugin = plugin;
        this.player = player;
        this.portalName = portalName;
        this.portalLocation = portalLocation;
        this.portalDestination = portalDestination;
        this.particle = particle;
        this.inventory = Bukkit.createInventory(null, 27, "Select Particle Amount");

        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeItems();
    }

    public void open() {
        player.openInventory(inventory);
    }

    private void initializeItems() {
        for (int i = 1; i <= 10; i++) {
            ItemStack item = new ItemStack(Material.FIREWORK_STAR);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(String.valueOf(i * 10));
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.FIREWORK_STAR) {
                int particleAmount = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage("Portal particle amount set to " + particleAmount);
                player.closeInventory();
                new PotionEffectMenu(plugin, player, portalName, portalLocation, portalDestination, particle, particleAmount).open();
            }
        }
    }
}
