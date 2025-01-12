package com.yourname.essentials;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    private final EssentialsPlugin plugin;
    private final Map<String, Map<String, Location>> warps = new HashMap<>();
    private final File warpsFile;
    private final FileConfiguration warpsConfig;

    public WarpManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.warpsFile = new File(plugin.getDataFolder(), "warps.yml");
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
        loadWarps();
    }

    public Map<String, Map<String, Location>> getWarps() {
        return warps;
    }

    public void setWarp(String category, String warpName, Location location) {
        warps.computeIfAbsent(category, k -> new HashMap<>()).put(warpName, location);
        saveWarps();
    }

    public void deleteWarp(String category, String warpName) {
        if (warps.containsKey(category)) {
            warps.get(category).remove(warpName);
            if (warps.get(category).isEmpty()) {
                warps.remove(category);
            }
            saveWarps();
        }
    }

    private void loadWarps() {
        for (String category : warpsConfig.getKeys(false)) {
            Map<String, Location> categoryWarps = new HashMap<>();
            for (String warpName : warpsConfig.getConfigurationSection(category).getKeys(false)) {
                Location location = warpsConfig.getLocation(category + "." + warpName);
                categoryWarps.put(warpName, location);
            }
            warps.put(category, categoryWarps);
        }
    }

    private void saveWarps() {
        for (Map.Entry<String, Map<String, Location>> categoryEntry : warps.entrySet()) {
            String category = categoryEntry.getKey();
            for (Map.Entry<String, Location> warpEntry : categoryEntry.getValue().entrySet()) {
                warpsConfig.set(category + "." + warpEntry.getKey(), warpEntry.getValue());
            }
        }
        try {
            warpsConfig.save(warpsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save warps data!");
            e.printStackTrace();
        }
    }
}
