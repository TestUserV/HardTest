package oljek.hd.object;

import com.oljek.main.util.StringUtil;
import oljek.hd.Hard;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class AnimatedDisplay extends BukkitRunnable {

    public AnimatedScoreboard scoreboard;
    private int cursor;
    private List<String> listDisplay;

    public AnimatedDisplay(AnimatedScoreboard scoreboard) {
        this.scoreboard = scoreboard;

        listDisplay = Hard.getInstance().getStorage().sbDisplayAnimated.stream()
                .map((s) -> StringUtil.inColor(s))
                .collect(Collectors.toList());
    }

    public void stopUpdate() {
        scoreboard = null;
        cancel();
    }

    @Override
    public void run() {
        if (cursor >= listDisplay.size() - 1)
            cursor = 0;

        String display = listDisplay.get(++cursor);
        scoreboard.getSidebar().setDisplayName(display);
    }

    public AnimatedScoreboard getScoreboard() {
        return scoreboard;
    }

}
