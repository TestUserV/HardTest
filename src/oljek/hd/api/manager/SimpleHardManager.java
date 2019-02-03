package oljek.hd.api.manager;

import com.oljek.spigot.manager.ConfigManager;
import oljek.hd.Hard;
import oljek.hd.object.Kit;
import oljek.hd.object.KitPlayer;
import oljek.hd.object.Setting;
import oljek.hd.object.Warp;
import oljek.hd.object.enums.ResultOperation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleHardManager implements HardManager {

    private Hard hard;

    public SimpleHardManager(Hard hard) {
        this.hard = hard;
    }

    @Override
    public ResultOperation set(String owner, String name, Location loc, boolean isWarp) {
        ConfigManager user = getCfg(owner);

        String locationCfg = (isWarp ? "warp." : "home.");

        checkSection(user, locationCfg.replace(".", ""));

        if (user.getConfiguration().contains(locationCfg + name))
            return ResultOperation.NAME_CONTAINS;

        user.set(locationCfg + name + ".location.world", loc.getWorld().getName());
        user.set(locationCfg + name + ".location.x", loc.getX());
        user.set(locationCfg + name + ".location.y", loc.getY());
        user.set(locationCfg + name + ".location.z", loc.getZ());
        user.set(locationCfg + name + ".location.yaw", loc.getYaw());
        user.set(locationCfg + name + ".location.pitch", loc.getPitch());
        user.save();

        return ResultOperation.ADD;
    }

    @Override
    public ResultOperation delete(String owner, String name, boolean isWarp) {
        ConfigManager user = getCfg(owner);

        String locationCfg = (isWarp ? "warp." : "home.");

        checkSection(user, locationCfg.replace(".", ""));

        if (!user.getConfiguration().contains(locationCfg + name))
            return ResultOperation.NOT_CONTAINS;

        user.set(locationCfg + name, null);
        user.save();

        return ResultOperation.DELETE;
    }

    @Override
    public Location get(String owner, String name, boolean isWarp) {
        ConfigManager user = getCfg(owner);

        String locationCfg = (isWarp ? "warp." : "home.");

        checkSection(user, locationCfg.replace(".", ""));

        if (!user.getConfiguration().contains(locationCfg + name + ".location"))
            return null;

        ConfigurationSection section = user.getConfiguration().getConfigurationSection(locationCfg + name + ".location");

        String world = section.getString("world");
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        double yaw = section.getDouble("yaw");
        double pitch = section.getDouble("pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
    }

    @Override
    public String[] getAllForPlayer(String owner, boolean isWarp) {
        ConfigManager user = getCfg(owner);
        user.create();

        String locationCfg = (isWarp ? "warp" : "home");

        checkSection(user, locationCfg);

        List<String> homes = user.getConfiguration().getConfigurationSection(locationCfg).getKeys(false)
                .stream()
                .filter((s) -> user.getConfiguration().contains(locationCfg + "." + s + ".location"))
                .collect(Collectors.toList());

        if (homes == null || homes.size() < 1)
            return new String[0];

        return homes.toArray(new String[homes.size()]);
    }

    @Override
    public Warp[] getAll(boolean isWarp) {
        File folder = new File(hard.getDataFolder(), "users");

        if (!folder.exists())
            return new Warp[0];

        if (folder.listFiles() == null || folder.listFiles().length < 1)
            return new Warp[0];

        List<Warp> warps = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (file.getName().contains(".yml")) {
                    String name = file.getName().replace(".yml", "");

                    ConfigManager cfg = new ConfigManager(hard, "users/" + name);
                    cfg.create();

                    checkSection(cfg, (isWarp ? "warp" : "home"));

                    for (String s : cfg.getConfiguration().getConfigurationSection((isWarp ? "warp" : "home")).getKeys(false)) {
                        Location loc = get(name, s, isWarp);
                        Warp warp = new Warp(name, s, loc);

                        warps.add(warp);
                    }
                }
            }
        }

        return warps.toArray(new Warp[warps.size()]);
    }

    @Override
    public int amount(String owner, boolean isWarp) {
        ConfigManager user = getCfg(owner);

        String locationCfg = (isWarp ? "warp" : "home");

        checkSection(user, locationCfg);

        int amount = (int) user.getConfiguration().getConfigurationSection(locationCfg).getKeys(false)
                .stream()
                .filter((s) -> user.getConfiguration().contains(locationCfg + "." + s + ".location"))
                .count();

        return amount;
    }

    @Override
    public Location getSpawn() {
        ConfigManager cfg = new ConfigManager(hard, "temp");
        cfg.create();

        if (!cfg.getConfiguration().contains("location.spawn"))
            return null;

        String world = cfg.getConfiguration().getString("location.spawn.world");
        double x = cfg.getConfiguration().getDouble("location.spawn.x");
        double y = cfg.getConfiguration().getDouble("location.spawn.y");
        double z = cfg.getConfiguration().getDouble("location.spawn.z");
        double yaw = cfg.getConfiguration().getDouble("location.spawn.yaw");
        double pitch = cfg.getConfiguration().getDouble("location.spawn.pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
    }

    @Override
    public void setSpawn(Location loc) {
        ConfigManager cfg = new ConfigManager(hard, "temp");
        cfg.create();

        cfg.set("location.spawn.world", loc.getWorld().getName());
        cfg.set("location.spawn.x", loc.getX());
        cfg.set("location.spawn.y", loc.getY());
        cfg.set("location.spawn.z", loc.getZ());
        cfg.set("location.spawn.yaw", loc.getYaw());
        cfg.set("location.spawn.pitch", loc.getPitch());
        cfg.save();
    }

    @Override
    public Kit getKit(String name) {
        File file = new File(hard.getDataFolder(), "kits/" + name + ".yml");

        if (!file.exists())
            return null;

        ConfigManager.createFolder(hard, "kits");
        ConfigManager cfg = new ConfigManager(hard, "kits/" + name);
        cfg.create();

        return new Kit(name, cfg);
    }

    @Override
    public Kit[] getAllKits() {
        File folder = new File(hard.getDataFolder(), "kits");

        if (!folder.exists())
            return new Kit[0];

        if (folder.listFiles() == null || folder.listFiles().length < 1)
            return new Kit[0];

        List<Kit> kits = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().contains(".yml")) {
                String nameKit = file.getName().replace(".yml", "");

                Kit kit = getKit(nameKit);
                kits.add(kit);
            }
        }

        return kits.toArray(new Kit[kits.size()]);
    }

    @Override
    public KitPlayer getPlayerKit(String player) {
        return new KitPlayer(player, hard);
    }

    @Override
    public ResultOperation createKit(String name, String donateGroup, boolean isWeak) {
        File file = new File(hard.getDataFolder(), "kits/" + name + ".yml");

        if (file.exists())
            return ResultOperation.NAME_CONTAINS;

        ConfigManager.createFolder(hard, "kits");
        ConfigManager cfg = new ConfigManager(hard, "kits/" + name);
        cfg.create();

        cfg.set("temp.donateGroup", donateGroup);
        cfg.set("temp.isWeak", isWeak);
        cfg.save();

        return ResultOperation.ADD;
    }

    @Override
    public ResultOperation deleteKit(String name) {
        File file = new File(hard.getDataFolder(), "kits/" + name + ".yml");

        if (!file.exists())
            return ResultOperation.NOT_CONTAINS;

        file.delete();
        return ResultOperation.DELETE;
    }

    @Override
    public void setPlayerTime(Player p, int hour, int minute) {
        p.setPlayerTime((hour * 1000) + ((999 * minute) / 60), false);
    }

    @Override
    public Setting getSetting(String owner) {
        Setting setting = new Setting(hard, owner);
        setting.load();

        return setting;
    }

    @Override
    public void writeIPAddress(String owner, String ipAddress) {
        ConfigManager.createFolder(hard, "users");
        ConfigManager cfg = new ConfigManager(hard, "users/" + owner);
        cfg.create();

        cfg.set("ipAddress", ipAddress);
        cfg.save();
    }

    @Override
    public String getIPAddress(String owner) {
        ConfigManager.createFolder(hard, "users");
        ConfigManager cfg = new ConfigManager(hard, "users/" + owner);
        cfg.create();

        return cfg.getConfiguration().getString("ipAddress", "0.0.0.0");
    }

    private ConfigManager getCfg(String owner) {
        ConfigManager.createFolder(hard, "users");
        ConfigManager user = new ConfigManager(hard, "users/" + owner);
        user.create();

        return user;
    }

    private void checkSection(ConfigManager cfg, String locationCfg) {
        if (!cfg.getConfiguration().contains(locationCfg)) {
            cfg.getConfiguration().createSection(locationCfg);
            cfg.save();
        }
    }

}
