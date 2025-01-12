package com.yourname.essentials.listeners;

import com.yourname.essentials.EssentialsPlugin;
import com.yourname.essentials.PortalManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {

    private final EssentialsPlugin plugin;

    public PortalListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from == null || to == null) return; // Add null check for 'from' and 'to'
        for (PortalManager.Portal portal : plugin.getPortalManager().getPortals().values()) {
            if (portal.getLocation().distance(to) < 1) {
                player.teleport(portal.getDestination());
                player.sendMessage("You have been teleported!");
                plugin.getLogger().info(player.getName() + " teleported to " + portal.getDestination()); // Add logging
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerMoveParticles(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from == null || to == null) return; // Add null check for 'from' and 'to'
        for (PortalManager.Portal portal : plugin.getPortalManager().getPortals().values()) {
            if (portal.getLocation().distance(to) < 5) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 2; y++) {
                        Location particleLocation = portal.getLocation().clone().add(x, y, 0);
                        player.spawnParticle(portal.getParticle(), particleLocation, portal.getParticleAmount());
                    }
                }
                break; // Avoid redundant particle spawning
            }
        }
    }
}
