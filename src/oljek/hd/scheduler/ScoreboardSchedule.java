package oljek.hd.scheduler;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.object.OneDate;
import eu.haelexuis.utils.xoreboard.XoreBoard;
import eu.haelexuis.utils.xoreboard.XoreBoardPlayerSidebar;
import eu.haelexuis.utils.xoreboard.XoreBoardUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import oljek.hd.object.ScoreboardStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardSchedule extends BukkitRunnable {

    private static final String[] animatedDisplay = new String[]{
            "&cHARD",
            "&c&lH&cARD",
            "&c&lHA&cRD",
            "&c&lHAR&cD",
            "&c&lHARD",
            "&c&lHAR&cD",
            "&c&lHA&cRD",
            "&c&lH&cARD",
            "&cHARD",
            "&6HARD",
            "&eHARD",
            "&2HARD",
            "&aHARD",
            "&cHARD",
            "&4HARD",
    };
    private XoreBoard xoreBoard;
    private Map<UUID, Integer> dateDisplay;
    private Map<UUID, OneDate<Long, ScoreboardStage>> stageScoreboard;

    public ScoreboardSchedule() {
        dateDisplay = new HashMap<>();
        stageScoreboard = new HashMap<>();
        xoreBoard = XoreBoardUtil.getNextXoreBoard();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            XoreBoardPlayerSidebar playerSidebar = xoreBoard.getSidebar(p);

            HashMap<String, Integer> lines = new HashMap<>();

            if (!stageScoreboard.containsKey(p.getUniqueId()))
                stageScoreboard.put(p.getUniqueId(), new OneDate<>(System.currentTimeMillis(), ScoreboardStage.GLOBAL_STATISTIC));
            else {
                if (System.currentTimeMillis() - stageScoreboard.get(p.getUniqueId()).getVar1() >= 5500l)
                    stageScoreboard.put(p.getUniqueId(), new OneDate<>(System.currentTimeMillis(), (stageScoreboard.get(p.getUniqueId()).getVar2() == ScoreboardStage.GLOBAL_STATISTIC ?
                            ScoreboardStage.BANK_STATISTIC : stageScoreboard.get(p.getUniqueId()).getVar2() == ScoreboardStage.BANK_STATISTIC ? ScoreboardStage.PVP_STATISTIC : ScoreboardStage.GLOBAL_STATISTIC)));
            }

            switch (stageScoreboard.get(p.getUniqueId()).getVar2()) {
                case GLOBAL_STATISTIC: {
                    lines.put("§7" + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()), 9);
                    lines.put("", 8);
                    lines.put("§fРанг: §f" + (UltraPermissions.getAPI().getUsers().name(p.getName()).getGroups().get().length < 1 ? "&cNONE&f" :
                            UltraPermissions.getAPI().getUsers().name(p.getName()).getGroups().get()[0].getName()), 8);
                    break;
                }

                case BANK_STATISTIC: {
                    lines.put("BANK", 1);
                    break;
                }

                case PVP_STATISTIC: {
                    lines.put("PVP", 1);
                    break;
                }
            }

            playerSidebar.rewriteLines(lines);

            changeDisplay(p, playerSidebar);

            playerSidebar.showSidebar();
        }
    }

    private void changeDisplay(Player p, XoreBoardPlayerSidebar sidebar) {
        if (p == null) {
            dateDisplay.remove(p.getUniqueId());
            return;
        }

        if (!dateDisplay.containsKey(p.getUniqueId()))
            dateDisplay.put(p.getUniqueId(), 0);
        else {
            if (dateDisplay.get(p.getUniqueId()) >= animatedDisplay.length - 1)
                dateDisplay.put(p.getUniqueId(), 0);
            else
                dateDisplay.put(p.getUniqueId(), dateDisplay.get(p.getUniqueId()) + 1);
        }

        sidebar.setDisplayName(StringUtil.inColor(animatedDisplay[dateDisplay.get(p.getUniqueId())]));
    }

}
