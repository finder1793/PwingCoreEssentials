package com.pwing.essentials.vanish;

import com.pwing.essentials.EssentialsPlugin;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class VanishManager extends PacketListenerAbstract {

    private EssentialsPlugin plugin;
    private Set<Player> vanishedPlayers;

    public VanishManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.vanishedPlayers = new HashSet<>();
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void setVanished(Player player, boolean vanished) {
        if (vanished) {
            vanishedPlayers.add(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    PacketEvents.getAPI().getPlayerManager().hidePlayer(onlinePlayer, player);
                }
            }
        } else {
            vanishedPlayers.remove(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    PacketEvents.getAPI().getPlayerManager().showPlayer(onlinePlayer, player);
                }
            }
        }
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_INFO) {
            Player player = event.getPlayer();
            if (isVanished(player)) {
                event.setCancelled(true);
            }
        }
    }
}
