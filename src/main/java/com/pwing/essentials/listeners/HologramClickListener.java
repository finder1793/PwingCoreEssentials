package com.pwing.essentials.listeners;

import com.pwing.essentials.EssentialsPlugin;
import com.pwing.essentials.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;

public class HologramClickListener implements Listener {

    private final EssentialsPlugin plugin;

    public HologramClickListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Implement click interaction logic
    }
}
