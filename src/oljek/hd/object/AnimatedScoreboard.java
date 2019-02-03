package oljek.hd.object;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.MainAPISpigot;
import com.oljek.spigot.object.OneDate;
import eu.haelexuis.utils.xoreboard.XoreBoardPlayerSidebar;
import oljek.economy.Economy;
import oljek.economy.api.Balance;
import oljek.economy.api.Card;
import oljek.hd.Hard;
import oljek.hd.date.ScoreboardDate;
import oljek.hd.util.RankUtil;
import oljek.rp.Rating;
import oljek.rp.util.StatisticUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AnimatedScoreboard extends BukkitRunnable {

    private Player p;
    private ScoreboardStage stage;
    private long lastChange;
    private AnimatedDisplay animatedDisplay;
    private XoreBoardPlayerSidebar sidebar;
    private double changeTimeGlobal;
    private double changeTimeBank;
    private double changeTimePVP;
    private List<OneDate<String, Long>> statisticMoney;
    private List<OneDate<String, OneDate<Long, Long>>> statisticPVP;
    private Setting setting;

    public AnimatedScoreboard(Player p) {
        this.p = p;

        stage = ScoreboardStage.GLOBAL_STATISTIC;

        if (!ScoreboardDate.scoreboard.getPlayers().contains(p))
            ScoreboardDate.scoreboard.addPlayer(p);

        sidebar = ScoreboardDate.scoreboard.getSidebar(p);
        changeTimeGlobal = Hard.getInstance().getStorage().sbChangeGlobalSpeed;
        changeTimeBank = Hard.getInstance().getStorage().sbChangeBankSpeed;
        changeTimePVP = Hard.getInstance().getStorage().sbChangePVPSpeed;
        statisticMoney = new LinkedList<>();
        statisticPVP = new LinkedList<>();
        animatedDisplay = new AnimatedDisplay(this);
        lastChange = System.currentTimeMillis();

        double speed = Hard.getInstance().getStorage().sbDisplaySpeed;

        animatedDisplay.runTaskTimer(Hard.getInstance(), 0, (long) (20 * speed));

        new BukkitRunnable() {

            @Override
            public void run() {
                if (p == null) {
                    cancel();
                    return;
                }

                if (statisticPVP == null || statisticMoney == null) {
                    cancel();
                    return;
                }

                setting = new Setting(Hard.getInstance(), p.getName());

                //PVPUpdate
                ResultSet resultPVP = MainAPISpigot.getInstance().getConnectionManager().query("SELECT * FROM `rating` ORDER BY `Kills` DESC LIMIT 10");

                statisticPVP.clear();

                try {
                    while (resultPVP.next()) {
                        String player = resultPVP.getString("Player");
                        long kills = resultPVP.getLong("Kills");
                        long death = resultPVP.getLong("Death");

                        statisticPVP.add(new OneDate<>(player, new OneDate<>(kills, death)));
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                //MoneyUpdate
                ResultSet resultMoney = MainAPISpigot.getInstance().getConnectionManager().query("SELECT * FROM `economy` ORDER BY `MoneyInBank` DESC LIMIT 10");

                statisticMoney.clear();

                try {
                    while (resultMoney.next()) {
                        String player = resultMoney.getString("Player");
                        long money = resultMoney.getLong("MoneyInBank");

                        statisticMoney.add(new OneDate<>(player, money));
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        }.runTaskTimer(Hard.getInstance(), 0, 20 * 60);
    }

    @Override
    public void run() {
        if (p == null || !p.isOnline() || !setting.isShowScoreboard()) {

            if (p != null) {
                ScoreboardDate.scoreboard.removePlayer(p);
                ScoreboardDate.animatedScoreboardDate.remove(p.getUniqueId());
            }


            animatedDisplay.stopUpdate();
            p = null;
            stage = null;
            lastChange = 0l;
            animatedDisplay = null;
            sidebar = null;
            changeTimeGlobal = 0l;
            changeTimeBank = 0l;
            changeTimePVP = 0l;
            statisticMoney = null;
            statisticPVP = null;
            cancel();
            return;
        }

        HashMap<String, Integer> lines = new HashMap<>();

        switch (stage) {
            case GLOBAL_STATISTIC: {
                if (System.currentTimeMillis() - lastChange >= changeTimeGlobal * 1000l) {
                    lastChange = System.currentTimeMillis();
                    stage = ScoreboardStage.PVP_STATISTIC;
                }

                String prefix = RankUtil.getPrefix(p.getName());
                ;

                if (prefix.isEmpty() || oljek.hd.util.StringUtil.stripColor(prefix, '&').isEmpty())
                    prefix = "&7Игрок";

                prefix = StringUtil.inColor(prefix);

                List<String> global = Hard.getInstance().getStorage().sbGlobalContent;

                for (int i = 0; i < global.size(); i++) {
                    int a = global.size() - 1 - i;
                    String s = global.get(i);

                    if (s.contains("$ifcard")) {
                        Card card = Economy.getInstance().getEconomyManager().getCard(p.getName());

                        if (card.getOwner().isEmpty())
                            continue;

                        s = s.replace("$ifcard", "")
                                .replace("$moneyCard", format(card.getBalance()) + "");
                    }

                    if (Economy.getInstance() == null || Economy.getInstance().getEconomyManager() == null)
                        return;

                    Balance balance = Economy.getInstance().getEconomyManager().getBalance(p.getName());
                    double kills = Rating.getInstance().getRatingManager().getKills(p.getName());
                    double death = Rating.getInstance().getRatingManager().getDeath(p.getName());
                    double kd = kills / (death == 0 ? 1 : death);

                    if (s.contains("$ifleague")) {
                        int numberLeague = StatisticUtil.getLeague(kd);

                        if (numberLeague == 0)
                            continue;

                        String league = StatisticUtil.toStringLeague(numberLeague, "ая");

                        s = s.replace("$ifleague", "")
                                .replace("$league", league + "§f");
                    }

                    String killsFormat = "";
                    String deathFormat = "";

                    Object[] killsObject = getMultiplier((long) kills);
                    Object[] deathObject = getMultiplier((long) death);

                    double killsMultiplier = (double) killsObject[0];
                    double deathMultiplier = (double) deathObject[0];

                    char killsChar = (char) killsObject[1];
                    char deathChar = (char) killsObject[1];

                    if (killsMultiplier != 0)
                        killsFormat = new DecimalFormat("#0.0").format(kills / killsMultiplier) + killsChar;
                    else
                        killsFormat = format(kills);

                    if (deathMultiplier != 0)
                        deathFormat = new DecimalFormat("#0.0").format(death / deathMultiplier) + deathChar;
                    else
                        deathFormat = format(death);

                    s = s.replace("$prefix", prefix)
                            .replace("$time", new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()))
                            .replace("$moneyGlobal", "" + format(Economy.getInstance().getEconomyManager().getBalance(p.getName()).getBalance()))
                            .replace("$moneyBank", format(balance.getMoneyInBank()) + "")
                            .replace("$kills", killsFormat + "")
                            .replace("$death", deathFormat + "")
                            .replace("$kd", new DecimalFormat("#0.00").format(kd) + "")
                            .replace("$online", Bukkit.getOnlinePlayers().size() + "");

                    s = StringUtil.inColor(s);

                    lines.put(s, a);
                }

                break;
            }

            case PVP_STATISTIC: {
                if (System.currentTimeMillis() - lastChange >= changeTimePVP * 1000l) {
                    lastChange = System.currentTimeMillis();
                    stage = ScoreboardStage.BANK_STATISTIC;
                }

                List<String> pvp = Hard.getInstance().getStorage().sbPVPContent;

                int pvpSize = (pvp.stream().anyMatch((s) -> s.contains("$rating")) ? pvp.size() + statisticPVP.size() : pvp.size());
                int lineCursor = -1;

                for (int i = pvpSize; i > 0; i--) {
                    String line = pvp.get(++lineCursor);

                    line = line.replace("$time", new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()));

                    if (line.contains("$rating")) {

                        int j = 1;

                        for (OneDate<String, OneDate<Long, Long>> datePVP : statisticPVP) {
                            long kills = datePVP.getVar2().getVar1();
                            long death = datePVP.getVar2().getVar2();

                            String killsFormat = "";
                            String deathFormat = "";

                            Object[] killsObject = getMultiplier(kills);
                            Object[] deathObject = getMultiplier(death);

                            double killsMultiplier = (double) killsObject[0];
                            double deathMultiplier = (double) deathObject[0];

                            char killsChar = (char) killsObject[1];
                            char deathChar = (char) killsObject[1];

                            if (killsMultiplier != 0)
                                killsFormat = new DecimalFormat("#0.0").format(kills / killsMultiplier) + killsChar;
                            else
                                killsFormat = format(kills);

                            if (deathMultiplier != 0)
                                deathFormat = new DecimalFormat("#0.0").format(death / deathMultiplier) + deathChar;
                            else
                                deathFormat = format(death);

                            String duplicateLine = line.replace("$statisticKill", killsFormat)
                                    .replace("$statisticDeath", deathFormat)
                                    .replace("$name", datePVP.getVar1())
                                    .replace("$number", j + "")
                                    .replace("$rating", "");

                            lines.put(duplicateLine, i - j + 1);
                            j++;
                        }

                        i -= j - 1;
                        continue;
                    }

                    lines.put(line, i);
                }
                break;
            }

            case BANK_STATISTIC: {
                if (System.currentTimeMillis() - lastChange >= changeTimeBank * 1000l) {
                    lastChange = System.currentTimeMillis();
                    stage = ScoreboardStage.GLOBAL_STATISTIC;
                }

                List<String> bank = Hard.getInstance().getStorage().sbBankContent;

                int bankSize = (bank.stream().anyMatch((s) -> s.contains("$rating")) ? bank.size() + statisticMoney.size() : bank.size());
                int lineCursor = -1;

                for (int i = bankSize; i > 0; i--) {
                    String line = bank.get(++lineCursor);

                    line = line.replace("$time", new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()));

                    if (line.contains("$rating")) {

                        int j = 1;

                        for (OneDate<String, Long> dateMoney : statisticMoney) {
                            long money = dateMoney.getVar2();
                            String moneyFormat = "";
                            Object[] moneyObject = getMultiplier(money);
                            double moneyMultiplier = (double) moneyObject[0];
                            char moneyChar = (char) moneyObject[1];

                            if (moneyMultiplier != 0)
                                moneyFormat = new DecimalFormat("#0.0").format(money / moneyMultiplier) + moneyChar;
                            else
                                moneyFormat = format(money);

                            String duplicateLine = line.replace("$statistic", moneyFormat)
                                    .replace("$name", dateMoney.getVar1())
                                    .replace("$number", j + "")
                                    .replace("$rating", "");

                            lines.put(duplicateLine, i - j + 1);
                            j++;
                        }

                        i -= j - 1;
                        continue;
                    }

                    lines.put(line, i);
                }
                break;
            }
        }

        sidebar.rewriteLines(lines);
        sidebar.showSidebar();
    }

    public XoreBoardPlayerSidebar getSidebar() {
        return sidebar;
    }

    public Player getPlayer() {
        return p;
    }

    private String format(double d) {
        return new DecimalFormat("###,###").format(d);
    }

    public void stopUpdate() {
        ScoreboardDate.scoreboard.removePlayer(p);
        p = null;
    }

    public boolean isUpdate() {
        return p != null;
    }

    private Object[] getMultiplier(long number) {
        double multiplier = 0d;
        char name = ' ';

        if (number >= 100000 && number < 1000000) {
            multiplier = 1000d;
            name = 'K';
        } else if (number >= 1000000 && number < 1000000000) {
            multiplier = 1000000d;
            name = 'M';
        } else if (number >= 1000000000d) {
            multiplier = 1000000000d;
            name = 'B';
        }

        return new Object[]{multiplier, name};
    }

}
