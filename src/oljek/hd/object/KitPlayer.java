package oljek.hd.object;

import com.google.common.collect.Lists;
import com.oljek.spigot.manager.ConfigManager;
import com.oljek.spigot.object.OneDate;
import oljek.hd.Hard;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitPlayer {

    private String name;
    private UUID uuid;
    private Map<String, Long> lastGive;
    private ConfigManager cfg;
    private Hard hard;

    public KitPlayer(String name, Hard hard) {
        this.name = name;
        this.hard = hard;

        ConfigManager.createFolder(hard, "users");
        cfg = new ConfigManager(hard, "users/" + name);
        cfg.create();

        uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

        lastGive = new HashMap<>();

        if (cfg.getConfiguration().contains("temp.lastGive")) {
            cfg.getConfiguration().getMapList("temp.lastGive")
                    .stream()
                    .forEach((s) -> s.entrySet()
                            .forEach((s2) -> {
                                ConfigManager.createFolder(hard, "kits");
                                ConfigManager kitCfg = new ConfigManager(hard, "kits/" + s2.getKey());
                                kitCfg.create();

                                lastGive.put((String) s2.getKey(), ((Map.Entry<String, Long>) s2).getValue());
                            }));
        }
    }

    public boolean canGive(Kit kit) {
        if (!lastGive.containsKey(kit.getName()))
            return true;

        long lastGive = this.lastGive.get(kit.getName());

        Calendar clWeek = addCurrent(Calendar.WEEK_OF_MONTH, 1, lastGive);
        Calendar clMonth = addCurrent(Calendar.MONTH, 1, lastGive);

        return (kit.isWeak() ? clWeek.getTimeInMillis() - System.currentTimeMillis() <= 0 : clMonth.getTimeInMillis() - System.currentTimeMillis() <= 0);
    }

    public String remainedTimeGive(Kit kit) {
        String formatTime = DurationFormatUtils.formatPeriod(System.currentTimeMillis(), lastGive.get(kit.getName()), "MM мес. dd д. HH ч. mm м. ss с.");

        formatTime = formatTime.replace("00 мес. ", "");
        formatTime = formatTime.replace("00 д. ", "");
        formatTime = formatTime.replace("00 ч. ", "");
        formatTime = formatTime.replace("00 м. ", "");
        formatTime = formatTime.replace("00 с.", "");

        return formatTime;
    }

    public void give(Kit kit) {
        if (kit == null)
            throw new NullPointerException("Kit is null!");

        Player p = Bukkit.getPlayer(name);

        if (p == null)
            throw new NullPointerException("Player: " + name + " not find!");

        int cursor = -1;
        Item cursorItem = null;

        for (OneDate<Integer, ItemStack> item : kit.getItems()) {
            for (int i = 0; i < item.getVar2().getAmount(); i++) {
                ItemStack stack = item.getVar2().clone();
                stack.setAmount(1);

                if (cursor != -1) {
                    if (p.getInventory().getItem(cursor).getAmount() < 64) {
                        p.getInventory().getItem(cursor).setAmount(p.getInventory().getItem(cursor).getAmount() + 1);
                        continue;
                    }
                }

                if (cursorItem != null) {
                    if (cursorItem.getItemStack().getAmount() >= 64) {
                        cursorItem = p.getWorld().dropItem(p.getLocation(), stack);
                        cursor = -1;
                    } else {
                        cursorItem.getItemStack().setAmount(cursorItem.getItemStack().getAmount() + 1);
                        cursor = -1;
                    }
                } else {
                    if (p.getInventory().firstEmpty() == -1) {
                        cursorItem = p.getWorld().dropItem(p.getLocation(), stack);
                        cursor = -1;
                    } else {
                        cursor = p.getInventory().firstEmpty();
                        p.getInventory().setItem(cursor, stack);
                    }
                }
            }

            cursorItem = null;
            cursor = -1;
        }

        setKit(kit);

        cfg.save();
    }

    public void setKit(Kit kit) {
        Map<String, Long> times = new HashMap<>();

        cfg.getConfiguration().getMapList("temp.lastGive")
                .stream()
                .forEach((s) -> s.entrySet()
                        .forEach((s2) -> {
                            ConfigManager.createFolder(hard, "kits");
                            ConfigManager kitCfg = new ConfigManager(hard, "kits/" + s2.getKey());
                            kitCfg.create();

                            times.put((String) s2.getKey(), ((Map.Entry<String, Long>) s2).getValue());
                        }));

        times.put(kit.getName(), (kit.isWeak() ? addCurrent(Calendar.WEEK_OF_MONTH, 1, -1).getTimeInMillis() : addCurrent(Calendar.MONTH, 1, -1).getTimeInMillis()));
        lastGive.put(kit.getName(), (kit.isWeak() ? addCurrent(Calendar.WEEK_OF_MONTH, 1, -1).getTimeInMillis() : addCurrent(Calendar.MONTH, 1, -1).getTimeInMillis()));

        cfg.set("temp.lastGive", Lists.newArrayList(times));
        cfg.save();
    }

    public UUID getUUID() {
        return uuid;
    }

    private Calendar addCurrent(int type, int amount, long defaultValue) {
        Calendar cl = Calendar.getInstance();

        if (defaultValue != -1)
            cl.setTimeInMillis(defaultValue);

        cl.add(type, amount);
        return cl;
    }

}
