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

public class PotionEffectMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final String portalName;
    private final Location portalLocation;
    private final Location portalDestination;
    private final Particle particle;
    private final int particleAmount;

    public PotionEffectMenu(EssentialsPlugin plugin, Player player, String portalName, Location portalLocation, Location portalDestination, Particle particle, int particleAmount) {
        this.plugin = plugin;
        this.player = player;
        this.portalName = portalName;
        this.portalLocation = portalLocation;
        this.portalDestination = portalDestination;
        this.particle = particle;
        this.particleAmount = particleAmount;
        this.inventory = Bukkit.createInventory(null, 54, "Select Potion Effect");

        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeItems();
    }

    public void open() {
        player.openInventory(inventory);
    }

    private void initializeItems() {
        for (PotionEffectType effectType : PotionEffectType.values()) {
            if (effectType != null) {
                ItemStack item = new ItemStack(Material.POTION);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(effectType.getName());
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.POTION) {
                String effectName = event.getCurrentItem().getItemMeta().getDisplayName();
                PotionEffectType effectType = PotionEffectType.getByName(effectName);
                player.sendMessage("Potion effect set to " + effectName);
                player.closeInventory();
                new PotionEffectDurationMenu(plugin, player, portalName, portalLocation, portalDestination, particle, particleAmount, effectType).open();
            }
        }
    }
}
