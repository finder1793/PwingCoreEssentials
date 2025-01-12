package com.yourname.essentials;

import com.yourname.essentials.commands.HomeCommand;
import com.yourname.essentials.commands.SetHomeCommand;
import com.yourname.essentials.commands.WarpCommand;
import com.yourname.essentials.commands.SetWarpCommand;
import com.yourname.essentials.commands.DelWarpCommand;
import com.yourname.essentials.commands.GameModeCommand;
import com.yourname.essentials.commands.SetPortalCommand;
import com.yourname.essentials.listeners.HomeMenuListener; // Ensure this import exists
import com.yourname.essentials.listeners.PortalListener;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EssentialsPlugin extends JavaPlugin {

    private HomeManager homeManager;
    private WarpManager warpManager;
    private PortalManager portalManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("EssentialsPlugin has been enabled!");
        createPlayerDataFolder();
        homeManager = new HomeManager(this);
        warpManager = new WarpManager(this);
        portalManager = new PortalManager(this);
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
        // Register other commands and events
        getServer().getPluginManager().registerEvents(new HomeMenuListener(this), this); // Ensure HomeMenuListener exists
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("EssentialsPlugin has been disabled!");
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

    private void createPlayerDataFolder() {
        File playerDataFolder = new File(getDataFolder(), "players");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }
}