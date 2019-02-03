package oljek.hd.api.manager;

import oljek.hd.object.Kit;
import oljek.hd.object.KitPlayer;
import oljek.hd.object.Setting;
import oljek.hd.object.Warp;
import oljek.hd.object.enums.ResultOperation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface HardManager {

    ResultOperation set(String owner, String name, Location loc, boolean isWarp);

    ResultOperation delete(String owner, String name, boolean isWarp);

    Location get(String owner, String name, boolean isWarp);

    String[] getAllForPlayer(String owner, boolean isWarp);

    Warp[] getAll(boolean isWarp);

    int amount(String owner, boolean isWarp);

    Location getSpawn();

    void setSpawn(Location loc);

    Kit getKit(String name);

    Kit[] getAllKits();

    KitPlayer getPlayerKit(String player);

    ResultOperation createKit(String name, String donateGroup, boolean isWeak);

    ResultOperation deleteKit(String name);

    void setPlayerTime(Player p, int hour, int minute);

    Setting getSetting(String owner);

    void writeIPAddress(String owner, String ipAddress);

    String getIPAddress(String owner);

}
