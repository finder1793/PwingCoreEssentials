package com.pwing.essentials;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final EssentialsPlugin plugin;
    private final Map<UUID, Map<String, Location>> homes = new HashMap<>();

    public HomeManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, Location> getHomes(Player player) {
        return homes.computeIfAbsent(player.getUniqueId(), k -> loadPlayerHomes(player));
    }

    public void setHome(Player player, String homeName, Location location) {
        getHomes(player).put(homeName, location);
        savePlayerHomes(player);
    }

    public boolean canSetHome(Player player) {
        int maxHomes = getMaxHomes(player);
        return getHomes(player).size() < maxHomes;
    }

    public int getMaxHomes(Player player) {
        if (player.hasPermission("essentials.sethome.limit.unlimited")) {
            return Integer.MAX_VALUE;
        } else if (player.hasPermission("essentials.sethome.limit.5")) {
            return 5;
        } else if (player.hasPermission("essentials.sethome.limit.3")) {
            return 3;
        } else if (player.hasPermission("essentials.sethome.limit.2")) {
            return 2;
        } else {
            return 1;
        }
    }

    private Map<String, Location> loadPlayerHomes(Player player) {
        File playerFile = getPlayerFile(player);
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        Map<String, Location> playerHomes = new HashMap<>();

        for (String homeName : playerConfig.getKeys(false)) {
            Location location = playerConfig.getLocation(homeName);
            playerHomes.put(homeName, location);
        }

        return playerHomes;
    }

    private void savePlayerHomes(Player player) {
        File playerFile = getPlayerFile(player);
        FileConfiguration playerConfig = new YamlConfiguration();

        Map<String, Location> playerHomes = homes.get(player.getUniqueId());
        for (Map.Entry<String, Location> entry : playerHomes.entrySet()) {
            playerConfig.set(entry.getKey(), entry.getValue());
        }

        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save homes data for player " + player.getName());
            e.printStackTrace();
        }
    }

    private File getPlayerFile(Player player) {
        return new File(plugin.getDataFolder(), "players" + File.separator + player.getUniqueId() + ".yml");
    }
}
