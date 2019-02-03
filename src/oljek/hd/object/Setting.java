package oljek.hd.object;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.manager.ConfigManager;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import oljek.hd.Hard;
import oljek.hd.date.ScoreboardDate;
import oljek.hd.util.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Setting {

    private Hard hard;
    private String owner;
    private ConfigManager cfg;
    private String joinMessage;
    private boolean onJoinMessage;
    private boolean showScoreboard;
    private boolean sendParticleWand;
    private boolean enableBlood;
    private boolean enableDynamicPrefix;
    private String nameAnimation;

    public Setting(Hard hard, String owner) {
        this.owner = owner;
        this.hard = hard;
    }

    public void load() {
        cfg = null;
        joinMessage = null;
        onJoinMessage = false;
        showScoreboard = false;
        sendParticleWand = false;
        enableBlood = false;
        enableDynamicPrefix = false;
        nameAnimation = null;

        ConfigManager.createFolder(hard, "users");
        cfg = new ConfigManager(hard, "users/" + owner);
        cfg.create();

        cfg.getConfiguration().addDefault("operation.join.message.enable", true);
        cfg.getConfiguration().addDefault("operation.join.message.content", "  &c&lSYSTEM &7>> &fК нам присоединился донатер: &c$name");
        cfg.getConfiguration().addDefault("options.showScoreboard", true);
        cfg.getConfiguration().addDefault("options.sendParticleWand", true);
        cfg.getConfiguration().addDefault("options.enableBlood", true);
        cfg.getConfiguration().addDefault("options.enableDynamicPrefix", false);
        cfg.getConfiguration().addDefault("options.nameAnimation", "");
        cfg.getConfiguration().options().copyDefaults(true);
        cfg.save();

        User user = UltraPermissions.getAPI().getUsers().name(owner);
        String prefix = RankUtil.getPrefix(user);

        joinMessage = StringUtil.inColor(cfg.getConfiguration().getString("operation.join.message.content").replace("$name", prefix + " " + owner));
        onJoinMessage = cfg.getConfiguration().getBoolean("operation.join.message.enable");
        showScoreboard = cfg.getConfiguration().getBoolean("options.showScoreboard");
        sendParticleWand = cfg.getConfiguration().getBoolean("options.sendParticleWand");
        enableBlood = cfg.getConfiguration().getBoolean("options.enableBlood");
        enableDynamicPrefix = cfg.getConfiguration().getBoolean("options.enableDynamicPrefix");
        nameAnimation = cfg.getConfiguration().getString("options.nameAnimation");
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        cfg.set("operation.join.message.content", joinMessage);
        cfg.save();

        this.joinMessage = joinMessage;
    }

    public boolean enableJoinMessage() {
        return onJoinMessage;
    }

    public boolean isShowScoreboard() {
        return showScoreboard;
    }

    public void setShowScoreboard(boolean showScoreboard) {
        cfg.set("options.showScoreboard", showScoreboard);
        cfg.save();

        this.showScoreboard = showScoreboard;

        Player p = Bukkit.getPlayer(owner);

        if (p == null || !p.isOnline())
            return;

        if (showScoreboard && !ScoreboardDate.animatedScoreboardDate.containsKey(p.getUniqueId())) {
            AnimatedScoreboard animatedScoreboard = new AnimatedScoreboard(p);

            animatedScoreboard.runTaskTimer(hard, 0, 20);
            ScoreboardDate.animatedScoreboardDate.put(p.getUniqueId(), animatedScoreboard);
        }
    }

    public boolean isSendParticleWand() {
        return sendParticleWand;
    }

    public void setSendParticleWand(boolean sendParticleWand) {
        cfg.set("options.sendParticleWand", sendParticleWand);
        cfg.save();

        this.sendParticleWand = sendParticleWand;
    }

    public boolean isEnableBlood() {
        return enableBlood;
    }

    public void setEnableBlood(boolean enableBlood) {
        cfg.set("options.enableBlood", enableBlood);
        cfg.save();

        this.enableBlood = enableBlood;
    }

    public boolean isEnableDynamicPrefix() {
        return enableDynamicPrefix;
    }

    public void setEnableDynamicPrefix(boolean enableDynamicPrefix) {
        cfg.set("options.enableDynamicPrefix", enableDynamicPrefix);
        cfg.save();

        this.enableDynamicPrefix = enableDynamicPrefix;
    }

    public String getNameAnimation() {
        return nameAnimation;
    }

    public void setNameAnimation(String nameAnimation) {
        cfg.set("options.nameAnimation", nameAnimation);
        cfg.save();

        this.nameAnimation = nameAnimation;
    }

    public void setEnableMessage(boolean onJoinMessage) {
        cfg.set("operation.join.message.enable", onJoinMessage);
        cfg.save();

        this.onJoinMessage = onJoinMessage;
    }

}
