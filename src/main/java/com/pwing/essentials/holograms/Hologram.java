package com.pwing.essentials.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

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
        // Use packets to display hologram to player
    }

    public void hide(Player player) {
        // Use packets to hide hologram from player
    }
}
