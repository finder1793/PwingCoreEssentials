package com.pwing.essentials;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortalManager {

    private final EssentialsPlugin plugin;
    private final Map<String, Portal> portals = new HashMap<>();
    private final File portalsFile;
    private final FileConfiguration portalsConfig;

    public PortalManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.portalsFile = new File(plugin.getDataFolder(), "portals.yml");
        this.portalsConfig = YamlConfiguration.loadConfiguration(portalsFile);
        loadPortals();
    }

    public Map<String, Portal> getPortals() {
        return portals;
    }

    public void setPortal(String name, Location location, Location destination, Particle particle, int particleAmount, PotionEffect potionEffect, boolean activateOnEntry) {
        portals.put(name, new Portal(location, destination, particle, particleAmount, potionEffect, activateOnEntry));
        savePortals();
    }

    public void deletePortal(String name) {
        portals.remove(name);
        savePortals();
    }

    private void loadPortals() {
        for (String name : portalsConfig.getKeys(false)) {
            Location location = portalsConfig.getLocation(name + ".location");
            Location destination = portalsConfig.getLocation(name + ".destination");
            Particle particle = Particle.valueOf(portalsConfig.getString(name + ".particle"));
            int particleAmount = portalsConfig.getInt(name + ".particleAmount");
            PotionEffectType potionEffectType = PotionEffectType.getByName(portalsConfig.getString(name + ".potionEffect.type"));
            int potionEffectDuration = portalsConfig.getInt(name + ".potionEffect.duration");
            int potionEffectAmplifier = portalsConfig.getInt(name + ".potionEffect.amplifier");
            PotionEffect potionEffect = new PotionEffect(potionEffectType, potionEffectDuration, potionEffectAmplifier);
            boolean activateOnEntry = portalsConfig.getBoolean(name + ".activateOnEntry");
            portals.put(name, new Portal(location, destination, particle, particleAmount, potionEffect, activateOnEntry));
        }
    }

    private void savePortals() {
        for (Map.Entry<String, Portal> entry : portals.entrySet()) {
            String name = entry.getKey();
            Portal portal = entry.getValue();
            portalsConfig.set(name + ".location", portal.getLocation());
            portalsConfig.set(name + ".destination", portal.getDestination());
            portalsConfig.set(name + ".particle", portal.getParticle().name());
            portalsConfig.set(name + ".particleAmount", portal.getParticleAmount());
            portalsConfig.set(name + ".potionEffect.type", portal.getPotionEffect().getType().getName());
            portalsConfig.set(name + ".potionEffect.duration", portal.getPotionEffect().getDuration());
            portalsConfig.set(name + ".potionEffect.amplifier", portal.getPotionEffect().getAmplifier());
            portalsConfig.set(name + ".activateOnEntry", portal.isActivateOnEntry());
        }
        try {
            portalsConfig.save(portalsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save portals data!");
            e.printStackTrace();
        }
    }

    public static class Portal {
        private final Location location;
        private final Location destination;
        private final Particle particle;
        private final int particleAmount;
        private final PotionEffect potionEffect;
        private final boolean activateOnEntry;

        public Portal(Location location, Location destination, Particle particle, int particleAmount, PotionEffect potionEffect, boolean activateOnEntry) {
            this.location = location;
            this.destination = destination;
            this.particle = particle;
            this.particleAmount = particleAmount;
            this.potionEffect = potionEffect;
            this.activateOnEntry = activateOnEntry;
        }

        public Location getLocation() {
            return location;
        }

        public Location getDestination() {
            return destination;
        }

        public Particle getParticle() {
            return particle;
        }

        public int getParticleAmount() {
            return particleAmount;
        }

        public PotionEffect getPotionEffect() {
            return potionEffect;
        }

        public boolean isActivateOnEntry() {
            return activateOnEntry;
        }
    }
}
