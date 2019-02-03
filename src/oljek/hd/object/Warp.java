package oljek.hd.object;

import org.bukkit.Location;

public class Warp {

    private String owner;
    private String name;
    private Location loc;

    public Warp(String owner, String name, Location loc) {
        this.owner = owner;
        this.name = name;
        this.loc = loc;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public Location getLoc() {
        return loc;
    }

}
