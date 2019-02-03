package oljek.hd.date;

import eu.haelexuis.utils.xoreboard.XoreBoard;
import oljek.hd.object.AnimatedScoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardDate {

    public transient static XoreBoard scoreboard;
    public transient static Map<UUID, AnimatedScoreboard> animatedScoreboardDate;

    static {
        animatedScoreboardDate = new HashMap<>();
    }

}
