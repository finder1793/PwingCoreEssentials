package com.pwing.essentials.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packetwrapper.play.server.WrapperPlayServerEntityDestroy;
import com.github.retrooper.packetevents.protocol.packetwrapper.play.server.WrapperPlayServerSpawnEntityLiving;
import com.github.retrooper.packetevents.protocol.packetwrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityDestroy;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntityLiving;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.nbt.NBTValue;

public class Hologram {
    private final Location location;
    private final List<String> lines;

    public Hologram(Location location, List<String> lines) {
        this.location = location;
        this.lines = lines;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getLines() {
        return lines;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void removeLine(int index) {
        lines.remove(index);
    }

    public void setLine(int index, String line) {
        lines.set(index, line);
    }

    public void display(Player player) {
        List<Integer> entityIds = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            int entityId = PacketEvents.get().getEntityManager().generateEntityId();
            entityIds.add(entityId);
            Location lineLocation = location.clone().add(0, -i * 0.25, 0);
            WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving(
                    entityId, UUID.randomUUID(), EntityType.ARMOR_STAND, lineLocation);
            WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, Collections.singletonList(
                    new NBTCompound().put("Invisible", new NBTValue<>(NBTType.BYTE, (byte) 1))
                            .put("CustomNameVisible", new NBTValue<>(NBTType.BYTE, (byte) 1))
                            .put("CustomName", new NBTValue<>(NBTType.STRING, "{\"text\":\"" + lines.get(i) + "\"}"))
            ));
            PacketEvents.get().getPlayerManager().sendPacket(player, spawnPacket);
            PacketEvents.get().getPlayerManager().sendPacket(player, metadataPacket);
        }
        player.setMetadata("hologram_entities", new FixedMetadataValue(plugin, entityIds));
    }

    public void hide(Player player) {
        if (player.hasMetadata("hologram_entities")) {
            List<Integer> entityIds = (List<Integer>) player.getMetadata("hologram_entities").get(0).value();
            WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy(entityIds);
            PacketEvents.get().getPlayerManager().sendPacket(player, destroyPacket);
            player.removeMetadata("hologram_entities", plugin);
        }
    }
}
