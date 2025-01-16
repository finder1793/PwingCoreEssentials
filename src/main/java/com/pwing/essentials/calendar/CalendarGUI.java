package com.pwing.essentials.calendar;

import com.pwing.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Calendar;
import java.util.Map;

public class CalendarGUI implements Listener {

    private static final String CALENDAR_TITLE = ChatColor.GREEN + "Calendar: ";

    private EssentialsPlugin plugin;
    private Inventory inventory;
    private String currentMonth;
    private int daysInMonth;

    public CalendarGUI(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.currentMonth = getCurrentMonth();
        this.daysInMonth = getDaysInMonth(currentMonth);
        this.inventory = Bukkit.createInventory(null, 54, CALENDAR_TITLE + currentMonth);
        loadCalendarItems();
    }

    public static void openCalendar(Player player, EssentialsPlugin plugin) {
        CalendarGUI calendarGUI = new CalendarGUI(plugin);
        Bukkit.getPluginManager().registerEvents(calendarGUI, plugin);
        player.openInventory(calendarGUI.getInventory());
    }

    private void loadCalendarItems() {
        for (int day = 1; day <= daysInMonth; day++) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Day " + day);
            item.setItemMeta(meta);
            inventory.setItem(day - 1, item);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        int monthIndex = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        Map<String, String> monthNames = plugin.getCalendarManager().getMonthNames();
        return monthNames.getOrDefault(String.valueOf(monthIndex), "Unknown");
    }

    private int getDaysInMonth(String month) {
        Map<String, Integer> monthLengths = plugin.getCalendarManager().getMonthLengths();
        return monthLengths.getOrDefault(month, 30); // Default to 30 days if not found
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(CALENDAR_TITLE)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                int day = slot + 1;
                String date = currentMonth + " " + day;
                String eventCommand = plugin.getCalendarManager().getEvent(date);
                if (eventCommand != null) {
                    player.sendMessage(ChatColor.GREEN + "Event for " + date + ": " + eventCommand);
                } else {
                    player.sendMessage(ChatColor.RED + "No event for " + date);
                }
            }
        }
    }
}
