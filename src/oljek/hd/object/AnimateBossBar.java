package oljek.hd.object;

import oljek.hd.Hard;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class AnimateBossBar extends BukkitRunnable {

    private Player p;
    private List<AnimateBossBarLine> animatedString;
    private int cursor;
    private BossBar bar;
    private long lastUpdate;

    public AnimateBossBar(Player p) {
        this.p = p;

        animatedString = Hard.getInstance().getStorage().bossBarAnimate.stream()
                .map((s) -> AnimateBossBarLine.fromLine(s))
                .collect(Collectors.toList());

        cursor = 0;

        AnimateBossBarLine line = animatedString.get(0);
        bar = Bukkit.createBossBar(line.getMessage()
                .replace("$player", p.getName())
                .replace("$online", Bukkit.getOnlinePlayers().size() + ""), line.getColor(), BarStyle.SOLID);
        bar.addPlayer(p);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (p == null) {
            bar.removeAll();
            p = null;
            animatedString = null;
            cursor = 0;
            bar = null;
            cancel();
            return;
        }

        if (cursor >= animatedString.size() - 1)
            cursor = 0;

        if (System.currentTimeMillis() - lastUpdate < 1000 * animatedString.get(cursor).getUpdateTime())
            return;
        else
            lastUpdate = System.currentTimeMillis();

        AnimateBossBarLine line = animatedString.get(cursor);

        bar.setTitle(line.getMessage()
                .replace("$player", p.getName())
                .replace("$online", Bukkit.getOnlinePlayers().size() + ""));
        bar.setProgress(line.getValueTime());
        bar.setColor(line.getColor());
        cursor++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        bar.removeAll();
    }

}
