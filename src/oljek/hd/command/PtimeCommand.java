package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("ptime")
public class PtimeCommand {

    private Hard hard;

    public PtimeCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("ptime"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime &e$time &7(&b8:30&7) &7- установить время только себе"));
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime time &7- узнать свое время"));
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime clear &7- установить такое же время как и на сервере"));

            if (p.hasPermission(hard.getStorage().permissions.get("ptime_other"))) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime &e$player $time &7(&b8:30&7) &7- установить время только указаному игроку"));
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime &e$player &ftime &7(&b8:30&7) &7- узнать время игрока"));
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/ptime &e$player &fclear &7- установить такое же время как и на сервере указаному игроку"));
            }

            return;
        }

        if (args.length == 1) {
            String timeNoSplit = args[0];

            if (timeNoSplit.contains(":")) {
                String[] timeSplit = timeNoSplit.split(":");
                Integer[] time = getTime(p, timeSplit);

                if (time.length == 0)
                    return;

                hard.getHardManager().setPlayerTime(p, time[0], time[1]);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно поставили себе: &c" + time[0] + " часов " + time[1] + " минут!"));
                return;
            }

            if (timeNoSplit.equals("clear")) {
                p.setPlayerTime(p.getWorld().getTime(), true);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТеперь Ваше время синхронизировано с серверным!"));
                return;
            }

            if (timeNoSplit.equals("time")) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВаше время состовляет: &c" + (p.getPlayerTime() / 1000) + ":" + (p.getPlayerTime() / 100)));
                return;
            }
        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players");
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Integer[] getTime(Player p, String[] timeSplit) {
        if (timeSplit.length != 2) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВремя должно указаться в 2 значениях! Пример: &b8:30"));
            return new Integer[0];
        }

        if (!isInt(timeSplit[0]) || !isInt(timeSplit[1])) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fУказаный Вами аргумент не является временем!"));
            return new Integer[0];
        }


        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        if (hour > 24) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fЗначение часа не должно быть больше 24!"));
            return new Integer[0];
        }

        if (minute > 60) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fЗначение минуты не должно быть больше 60!"));
            return new Integer[0];
        }

        return new Integer[]{hour, minute};
    }

}
