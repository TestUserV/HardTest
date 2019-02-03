package oljek.hd.object;

import oljek.cl.ClanPlugin;
import oljek.cl.util.TeamUtil;
import oljek.hd.Hard;
import oljek.hd.date.AnimateDate;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimatedPrefix extends BukkitRunnable {

    private List<String> animateString;
    private AnimateDate.AnimateCursor animateCursor;
    private int priority;

    public AnimatedPrefix(Hard hard, Player p, String nameAnimation, String group) {
        AnimateDate date = hard.getAnimateStorage().getAnimatedPrefix().get(group.toLowerCase());
        Optional<ArrayAnimateLine> object = date.getAnimateLines().stream().filter((s) -> s.getName().toLowerCase().equals(nameAnimation.toLowerCase())).findAny();

        if (!object.isPresent())
            return;

        animateString = object.get().getLines();

        if (!AnimateDate.cursorsAnimations.containsKey(group.toLowerCase())) {
            AnimateDate.cursorsAnimations.put(group.toLowerCase(), new ArrayList<>());
            AnimateDate.cursorsAnimations.get(group.toLowerCase()).add(new AnimateDate.AnimateCursor(nameAnimation));
        } else {
            boolean find = AnimateDate.cursorsAnimations.get(group.toLowerCase())
                    .stream()
                    .anyMatch((a) -> a.getAnimateName().equals(nameAnimation));

            if (!find)
                AnimateDate.cursorsAnimations.get(group.toLowerCase())
                        .add(new AnimateDate.AnimateCursor(nameAnimation));
        }

        animateCursor = AnimateDate.cursorsAnimations.get(group.toLowerCase())
                .stream()
                .filter((a) -> a.getAnimateName().equals(nameAnimation))
                .findAny().get();

        priority = ClanPlugin.getSetting().getPriority().getOrDefault(group, 8);

        if (!animateCursor.getPlayers().contains(p))
            animateCursor.addPlayer(p);
        runTaskTimer(hard, 0, (long) (20 * date.getChangeTime()));
    }

    @Override
    public void run() {
        if (animateCursor.getPlayers().isEmpty())
            return;

        if (animateCursor.getCursor() >= animateString.size() - 1)
            animateCursor.setZeroCusror();

        for (Player p : animateCursor.getPlayers())
            TeamUtil.setTeam(p, priority, animateString.get(animateCursor.getCursor()));

        animateCursor.incrementCursor();
    }

}
