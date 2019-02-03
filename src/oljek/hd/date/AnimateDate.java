package oljek.hd.date;

import oljek.hd.object.ArrayAnimateLine;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimateDate {

    public static Map<String, List<AnimateCursor>> cursorsAnimations;

    static {
        cursorsAnimations = new HashMap<>();
    }

    private List<ArrayAnimateLine> animateLines;
    private double changeTime;

    public AnimateDate(List<ArrayAnimateLine> animateLines, double changeTime) {
        this.animateLines = animateLines;
        this.changeTime = changeTime;
    }

    public double getChangeTime() {
        return changeTime;
    }

    public List<ArrayAnimateLine> getAnimateLines() {
        return animateLines;
    }

    public static class AnimateCursor {

        private String animateName;
        private List<Player> players;
        private int cursor;

        public AnimateCursor(String animateName) {
            this.animateName = animateName;
            players = new ArrayList<>();
            cursor = 0;
        }

        public String getAnimateName() {
            return animateName;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public void setPlayers(List<Player> players) {
            this.players = players;
        }

        public int getCursor() {
            return cursor;
        }

        public void incrementCursor() {
            cursor++;
        }

        public void setZeroCusror() {
            cursor = 0;
        }

        public void addPlayer(Player p) {
            players.add(p);
        }

        public void removePlayer(Player p) {
            players.remove(p);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;

            if (!obj.getClass().getName().equals(this.getClass().getName()))
                return false;

            AnimateCursor animateCursor = (AnimateCursor) obj;

            if (!animateCursor.getAnimateName().equals(this.animateName))
                return false;

            return true;
        }
    }

}
