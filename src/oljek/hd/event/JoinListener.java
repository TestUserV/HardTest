package oljek.hd.event;

import com.google.common.collect.Lists;
import com.oljek.main.util.StringUtil;
import oljek.hd.Hard;
import oljek.hd.date.AnimateDate;
import oljek.hd.date.DefaultKit;
import oljek.hd.date.ScoreboardDate;
import oljek.hd.object.AnimateBossBar;
import oljek.hd.object.AnimatedPrefix;
import oljek.hd.object.AnimatedScoreboard;
import oljek.hd.object.Setting;
import oljek.hd.util.RankUtil;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JoinListener implements Listener {

    private Hard hard;
    private List<String[]> startAnimations;

    public JoinListener(Hard hard) {
        this.hard = hard;
        startAnimations = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        hard.getHardManager().writeIPAddress(p.getName(), ((InetSocketAddress)((CraftPlayer)p).getHandle().playerConnection.networkManager.l).getAddress().getHostAddress());

        Setting setting = hard.getHardManager().getSetting(p.getName());

        if (setting.isShowScoreboard()) {
            AnimatedScoreboard animatedScoreboard = new AnimatedScoreboard(p);

            animatedScoreboard.runTaskTimer(hard, 0, 20);
            ScoreboardDate.animatedScoreboardDate.put(p.getUniqueId(), animatedScoreboard);
        }

        new AnimateBossBar(p).runTaskTimer(hard, 0, 5);
        new CustomPlayerListener(p, hard);

        if (!p.hasPlayedBefore()) {
            e.setJoinMessage(StringUtil.inColor("  &7>> &c&lGLOBAL: &fУ нас новый игрок: &a" + p.getName() + ", &fпожелаем все удачи!"));

            for (ItemStack stack : DefaultKit.getItems(p.getName()))
                p.getInventory().addItem(stack);

            Location spawnLocation = Hard.getInstance().getHardManager().getSpawn();

            if (spawnLocation == null)
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСпавн временно не доступен!"));
            else
                p.teleport(spawnLocation);
        }

        List<String> groupsJoin = hard.getStorage().groupsMessageJoin;

        String groupName = RankUtil.getGroup(p.getName());

        if (groupsJoin.contains(groupName)) {
            if (setting.enableJoinMessage())
                e.setJoinMessage(setting.getJoinMessage());
            else
                e.setJoinMessage(null);
        } else
            e.setJoinMessage(null);

        List<String> motd = hard.getStorage().motd;

        if (!motd.isEmpty()) {
            String prefix = RankUtil.getPrefix(p.getName());

            for (String s : motd) {
                s = s.replace("$name", prefix);

                s = StringUtil.inColor(s);
                p.sendMessage(s);
            }
        }

        if (hard.getAnimateStorage().getAnimatedPrefix().containsKey(groupName.toLowerCase())) {
            if (setting.getNameAnimation() == null || setting.getNameAnimation().isEmpty())
                return;

            String[] arrayOfDate = new String[]{groupName.toLowerCase(), setting.getNameAnimation()};
            boolean findAnimation = startAnimations.stream().anyMatch((s) -> {
                if (s.length == arrayOfDate.length) {
                    for (int i = 0; i < s.length; i++) {
                        String arrayLine = arrayOfDate[i];
                        String animationLine = s[i];

                        if (!arrayLine.equals(animationLine))
                            return false;
                    }

                    return true;
                }

                return false;
            });

            if (!findAnimation) {
                startAnimations.add(arrayOfDate);

                new AnimatedPrefix(hard, p, setting.getNameAnimation(), groupName);
                return;
            }

            AnimateDate.AnimateCursor cursor;

            if (!AnimateDate.cursorsAnimations.containsKey(groupName.toLowerCase()))
                cursor = Objects.requireNonNull(AnimateDate.cursorsAnimations.put(groupName.toLowerCase(), new ArrayList<>(Lists.newArrayList(new AnimateDate.AnimateCursor(setting.getNameAnimation()))))).get(0);
            else {
                Optional<AnimateDate.AnimateCursor> optionalCursor = AnimateDate.cursorsAnimations.get(groupName.toLowerCase()).stream()
                        .filter((a) -> a.getAnimateName().equals(setting.getNameAnimation()))
                        .findAny();

                if (!optionalCursor.isPresent()) {
                    cursor = new AnimateDate.AnimateCursor(setting.getNameAnimation());

                    AnimateDate.cursorsAnimations.get(groupName.toLowerCase()).add(cursor);
                } else
                    cursor = optionalCursor.get();
            }

            if (!cursor.getPlayers().contains(p))
                cursor.addPlayer(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);

        Player p = e.getPlayer();
        String group = RankUtil.getGroup(p.getName());

        if (ScoreboardDate.animatedScoreboardDate.containsKey(p.getUniqueId()))
            ScoreboardDate.animatedScoreboardDate.remove(p.getUniqueId());

        if (!AnimateDate.cursorsAnimations.containsKey(group.toLowerCase()))
            return;

        AnimateDate.cursorsAnimations.get(group.toLowerCase()).forEach((a) -> {
            if (a.getPlayers().contains(p))
                a.removePlayer(p);
        });
    }

}
