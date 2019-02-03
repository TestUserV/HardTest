package oljek.hd.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isOnline(String target) {
        return Bukkit.getPlayer(target) != null;
    }

    public static Player getTarget(String target) {
        if (!isOnline(target))
            return null;

        return Bukkit.getPlayer(target);
    }

}
