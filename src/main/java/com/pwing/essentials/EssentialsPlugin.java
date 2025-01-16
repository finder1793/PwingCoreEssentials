package com.pwing.essentials;

import com.pwing.essentials.commands.HomeCommand;
import com.pwing.essentials.commands.SetHomeCommand;
import com.pwing.essentials.commands.WarpCommand;
import com.pwing.essentials.commands.SetWarpCommand;
import com.pwing.essentials.commands.DelWarpCommand;
import com.pwing.essentials.commands.GameModeCommand;
import com.pwing.essentials.commands.SetPortalCommand;
import com.pwing.essentials.commands.HologramCommand;
import com.pwing.essentials.commands.VanishCommand;
import com.pwing.essentials.commands.KitsCommand;
import com.pwing.essentials.commands.KitEditorCommand;
import com.pwing.essentials.commands.CalendarCommand;
import com.pwing.essentials.listeners.HomeMenuListener;
import com.pwing.essentials.listeners.PortalListener;
import com.pwing.essentials.listeners.HologramClickListener;
import com.pwing.essentials.vanish.VanishManager;
import com.pwing.essentials.kits.KitManager;
import com.pwing.essentials.calendar.CalendarManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EssentialsPlugin extends JavaPlugin {

    private HomeManager homeManager;
    private WarpManager warpManager;
    private PortalManager portalManager;
    private VanishManager vanishManager;
    private KitManager kitManager;
    private CalendarManager calendarManager;

    @Override
    public void onEnable() {
        // Initialize PacketEvents
        PacketEvents.create(SpigotPacketEventsBuilder.build(this)).init();
        
        // Plugin startup logic
        getLogger().info("EssentialsPlugin has been enabled!");
        createPlayerDataFolder();
        homeManager = new HomeManager(this);
        warpManager = new WarpManager(this);
        portalManager = new PortalManager(this);
        vanishManager = new VanishManager(this);
        kitManager = new KitManager(this);
        calendarManager = new CalendarManager(this);
        // Register commands
        this.getCommand("home").setExecutor(new HomeCommand(this));
        this.getCommand("sethome").setExecutor(new SetHomeCommand(this));
        this.getCommand("warp").setExecutor(new WarpCommand(this));
        this.getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        this.getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        this.getCommand("gm0").setExecutor(new GameModeCommand(GameMode.SURVIVAL));
        this.getCommand("gm1").setExecutor(new GameModeCommand(GameMode.CREATIVE));
        this.getCommand("gm2").setExecutor(new GameModeCommand(GameMode.ADVENTURE));
        this.getCommand("gm3").setExecutor(new GameModeCommand(GameMode.SPECTATOR));
        this.getCommand("setportal").setExecutor(new SetPortalCommand(this));
        this.getCommand("hologram").setExecutor(new HologramCommand(this));
        this.getCommand("vanish").setExecutor(new VanishCommand(this));
        this.getCommand("kits").setExecutor(new KitsCommand(this));
        this.getCommand("kiteditor").setExecutor(new KitEditorCommand(this));
        this.getCommand("calendar").setExecutor(new CalendarCommand(this));
        // Register other commands and events
        getServer().getPluginManager().registerEvents(new HomeMenuListener(this), this); // Ensure HomeMenuListener exists
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new HologramClickListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("EssentialsPlugin has been disabled!");
        PacketEvents.getAPI().terminate();
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public CalendarManager getCalendarManager() {
        return calendarManager;
    }

    private void createPlayerDataFolder() {
        File playerDataFolder = new File(getDataFolder(), "players");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }
}