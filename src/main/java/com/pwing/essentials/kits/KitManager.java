package com.pwing.essentials.kits;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private EssentialsPlugin plugin;
    private Map<String, Inventory> kits;

    public KitManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
        loadKits();
    }

    public boolean kitExists(String kitName) {
        return kits.containsKey(kitName);
    }

    public Inventory getKitInventory(String kitName) {
        return kits.get(kitName);
    }

    public boolean giveKit(Player player, String kitName) {
        Inventory kitInventory = kits.get(kitName);
        if (kitInventory == null) {
            return false;
        }

        for (ItemStack item : kitInventory.getContents()) {
            if (item != null && player.getInventory().firstEmpty() == -1) {
                return false;
            }
        }

        for (ItemStack item : kitInventory.getContents()) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }

        // Execute commands associated with the kit
        FileConfiguration config = plugin.getConfig();
        if (config.contains("kits." + kitName + ".commands")) {
            for (String command : config.getStringList("kits." + kitName + ".commands")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
            }
        }

        return true;
    }

    public void saveKit(String kitName, Inventory inventory) {
        kits.put(kitName, inventory);
        File kitFile = new File(plugin.getDataFolder(), "kits.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(kitFile);

        config.set("kits." + kitName + ".items", inventory.getContents());

        try {
            config.save(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadKits() {
        File kitFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitFile.exists()) {
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(kitFile);
        for (String kitName : config.getConfigurationSection("kits").getKeys(false)) {
            Inventory inventory = Bukkit.createInventory(null, 27, kitName);
            ItemStack[] items = ((List<ItemStack>) config.get("kits." + kitName + ".items")).toArray(new ItemStack[0]);
            inventory.setContents(items);
            kits.put(kitName, inventory);
        }
    }
}
