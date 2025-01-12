package com.yourname.essentials.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

    private final GameMode gameMode;

    public GameModeCommand(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String permission = "essentials.gamemode." + gameMode.name().toLowerCase();
            if (player.hasPermission(permission)) {
                player.setGameMode(gameMode);
                player.sendMessage("Game mode changed to " + gameMode.name().toLowerCase());
                return true;
            } else {
                player.sendMessage("You do not have permission to use this command.");
                return false;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}
