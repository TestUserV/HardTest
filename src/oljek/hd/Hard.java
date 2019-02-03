package oljek.hd;

import com.oljek.spigot.MainAPISpigot;
import com.oljek.spigot.command.custom.CommandHandler;
import com.oljek.spigot.config.Storage;
import com.oljek.spigot.manager.ConfigManager;
import eu.haelexuis.utils.xoreboard.XoreBoardUtil;
import oljek.hd.api.manager.HardManager;
import oljek.hd.api.manager.SimpleHardManager;
import oljek.hd.command.*;
import oljek.hd.date.DateMessage;
import oljek.hd.date.DeathDate;
import oljek.hd.date.DefaultKit;
import oljek.hd.date.ScoreboardDate;
import oljek.hd.event.*;
import oljek.hd.object.AnimateBossBar;
import oljek.hd.object.AnimatedScoreboard;
import oljek.hd.object.Setting;
import oljek.hd.storage.AnimateStorage;
import oljek.hd.storage.ConfigStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Hard extends JavaPlugin {

    private static Hard instance;
    private HardManager hardManager;
    private ConfigStorage storage;
    private AnimateStorage animateStorage;
    private boolean isEnable;

    public static Hard getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        new BukkitRunnable() {

            @Override
            public void run() {
                isEnable = true;
                hardManager = new SimpleHardManager(instance);

                Storage.addConfig(instance, new ConfigStorage());
                Storage.loadPlugin(instance);

                storage = MainAPISpigot.getClass(instance, ConfigStorage.class);
                animateStorage = new AnimateStorage(instance);

                DefaultKit.load();

                CommandHandler.registerCommand(new GamemodeCommand(instance));
                CommandHandler.registerCommand(new SetHomeCommand(instance));
                CommandHandler.registerCommand(new HomeCommand(instance));
                CommandHandler.registerCommand(new HelpCommand(instance));
                CommandHandler.registerCommand(new InfoCommand(instance));
                CommandHandler.registerCommand(new WarpCommand(instance));
                CommandHandler.registerCommand(new SpawnCommand(instance));
                CommandHandler.registerCommand(new SetSpawnCommand(instance));
                CommandHandler.registerCommand(new ListCommand(instance));
                CommandHandler.registerCommand(new MessageCommand());
                CommandHandler.registerCommand(new CallCommand(instance));
                CommandHandler.registerCommand(new KitCommand(instance));
                CommandHandler.registerCommand(new SuicideCommand(instance));
                CommandHandler.registerCommand(new FeedCommand(instance));
                CommandHandler.registerCommand(new WorkbenchCommand(instance));
                CommandHandler.registerCommand(new ClearInventoryCommand(instance));
                CommandHandler.registerCommand(new TopCommand(instance));
                CommandHandler.registerCommand(new BackCommand(instance));
                CommandHandler.registerCommand(new EnderchestCommand(instance));
                CommandHandler.registerCommand(new HealCommand(instance));
                CommandHandler.registerCommand(new FireworkCommand(instance));
                CommandHandler.registerCommand(new FlyCommand(instance));
                CommandHandler.registerCommand(new PtimeCommand(instance));
                CommandHandler.registerCommand(new RepairCommand(instance));
                CommandHandler.registerCommand(new ExtCommand(instance));
                CommandHandler.registerCommand(new GodCommand(instance));
                CommandHandler.registerCommand(new NearCommand(instance));
                CommandHandler.registerCommand(new PtimeCommand(instance));
                CommandHandler.registerCommand(new GeneralCommand(instance));
                CommandHandler.registerCommand(new HardCommand(instance));
                CommandHandler.registerCommand(new DynamicPrefixCommand(instance));
                CommandHandler.registerCommand(new FastMessageCommand(instance));
                CommandHandler.registerCommand(new TeleportCommand(instance));
                CommandHandler.registerCommand(new BreakCommand(instance));
                CommandHandler.registerCommand(new SpeedCommand(instance));

                Bukkit.getPluginManager().registerEvents(new DeathListener(), instance);
                Bukkit.getPluginManager().registerEvents(new UpdateListener(instance), instance);
                Bukkit.getPluginManager().registerEvents(new JoinListener(instance), instance);

                ConfigManager temp = new ConfigManager(instance, "temp");
                temp.create();

                ScoreboardDate.scoreboard = XoreBoardUtil.getNextXoreBoard();

                if (temp.getConfiguration().contains("godList")) {
                    DeathDate.godList = temp.getConfiguration().getStringList("godList").stream()
                            .map((s) -> UUID.fromString(s))
                            .collect(Collectors.toList());
                }

                for (Player p : Bukkit.getOnlinePlayers()) {
                    Setting setting = getHardManager().getSetting(p.getName());

                    if (setting.isShowScoreboard()) {
                        AnimatedScoreboard animatedScoreboard = new AnimatedScoreboard(p);

                        animatedScoreboard.runTaskTimer(instance, 0, 20);
                        ScoreboardDate.animatedScoreboardDate.put(p.getUniqueId(), animatedScoreboard);
                    }

                    new AnimateBossBar(p).runTaskTimer(instance, 0, 5);
                    new CustomPlayerListener(p, instance);
                }
            }

        }.runTaskLater(this, 20 * 3);

        Bukkit.getPluginManager().registerEvents(new JoinStartListener(this), this);
    }

    public void onDisable() {
        ConfigManager temp = new ConfigManager(this, "temp");
        temp.create();

        List<String> godList = new ArrayList<>();

        for (UUID uuid : DeathDate.godList)
            godList.add(uuid.toString());

        temp.set("godList", godList);
        temp.save();
    }

    public HardManager getHardManager() {
        return hardManager;
    }

    public ConfigStorage getStorage() {
        return storage;
    }

    public boolean checkPermission(Player p, String permission) {
        if (!p.hasPermission(getStorage().permissions.get(permission))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return false;
        }

        return true;
    }

    public AnimateStorage getAnimateStorage() {
        return animateStorage;
    }

    public void reloadAnimateStorage() {
        animateStorage = new AnimateStorage(this);
    }

    public boolean isEnable() {
        return isEnable;
    }

}
