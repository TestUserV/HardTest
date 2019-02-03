package oljek.hd.date;

import org.bukkit.Location;

import java.util.*;

public class DeathDate {

    public transient static Map<UUID, Location> lastDeathLocation;
    public transient static List<UUID> godList;

    static {
        lastDeathLocation = new HashMap<>();
        godList = new ArrayList<>();
    }

}
