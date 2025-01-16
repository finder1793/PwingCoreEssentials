package com.pwing.essentials.calendar;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarManager {

    private EssentialsPlugin plugin;
    private Map<String, String> events;
    private Map<String, Integer> monthLengths;
    private Map<String, String> monthNames;

    public CalendarManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.events = new HashMap<>();
        this.monthLengths = new HashMap<>();
        this.monthNames = new HashMap<>();
        loadConfig();
        startEventNotificationTask();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "calendar.yml");
        if (!configFile.exists()) {
            plugin.saveResource("calendar.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (String month : config.getConfigurationSection("months").getKeys(false)) {
            monthNames.put(month, config.getString("months." + month + ".name"));
            monthLengths.put(month, config.getInt("months." + month + ".length"));
        }

        for (String date : config.getConfigurationSection("events").getKeys(false)) {
            events.put(date, config.getString("events." + date));
        }
    }

    public void saveEvent(String date, String event) {
        events.put(date, event);
        File configFile = new File(plugin.getDataFolder(), "calendar.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.set("events." + date, event);

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEvent(String date) {
        return events.get(date);
    }

    public void runEvent(String date) {
        String event = events.get(date);
        if (event != null) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), event);
        }
    }

    public Map<String, Integer> getMonthLengths() {
        return monthLengths;
    }

    public Map<String, String> getMonthNames() {
        return monthNames;
    }

    private void startEventNotificationTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String currentDate = getCurrentMonth() + " " + calendar.get(Calendar.DAY_OF_MONTH);
                String event = getEvent(currentDate);
                if (event != null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Event today: " + event);
                }

                // Check for weekly events
                String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, plugin.getLocale());
                String weeklyEvent = getEvent("weekly." + dayOfWeek);
                if (weeklyEvent != null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Weekly event today: " + weeklyEvent);
                }

                // Check for monthly events
                String dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String monthlyEvent = getEvent("monthly." + dayOfMonth);
                if (monthlyEvent != null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Monthly event today: " + monthlyEvent);
                }

                // Check for yearly events
                String yearlyEvent = getEvent("yearly." + getCurrentMonth() + " " + dayOfMonth);
                if (yearlyEvent != null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Yearly event today: " + yearlyEvent);
                }
            }
        }.runTaskTimer(plugin, 0, 24000); // Run every Minecraft day
    }

    private String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        int monthIndex = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        return monthNames.getOrDefault(String.valueOf(monthIndex), "Unknown");
    }
}

