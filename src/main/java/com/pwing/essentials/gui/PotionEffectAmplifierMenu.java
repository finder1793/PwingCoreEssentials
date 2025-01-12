package com.pwing.essentials.gui;

import com.pwing.essentials.EssentialsPlugin;
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

public class PotionEffectAmplifierMenu implements Listener {

    private final EssentialsPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final String portalName;
    private final Location portalLocation;
    private final Location portalDestination;
    private final Particle particle;
    private final int particleAmount;
    private final PotionEffectType effectType;
    private final int duration;

    public PotionEffectAmplifierMenu(EssentialsPlugin plugin, Player player, String portalName, Location portalLocation, Location portalDestination, Particle particle, int particleAmount, PotionEffectType effectType, int duration) {
        this.plugin = plugin;
        this.player = player;
        this.portalName = portalName;
        this.portalLocation = portalLocation;
        this.portalDestination = portalDestination;
        this.particle = particle;
        this.particleAmount = particleAmount;
        this.effectType = effectType;
        this.duration = duration;
        this.inventory = Bukkit.createInventory(null, 27, "Select Effect Amplifier");

        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeItems();
    }

    public void open() {
        player.openInventory(inventory);
    }

    private void initializeItems() {
        for (int i = 1; i <= 5; i++) {
            ItemStack item = new ItemStack(Material.POTION);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(String.valueOf(i));
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.POTION) {
                int amplifier = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
                player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                player.sendMessage("Potion effect applied with amplifier: " + amplifier);
                player.closeInventory();
            }
        }
    }
}
