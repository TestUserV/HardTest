package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("speed")
public class SpeedCommand {

    private Hard hard;

    public SpeedCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecutor(Player p, String[] args) {
        if (!hard.checkPermission(p, "speed"))
            return;

        if (args.length < 1) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/speed &e$speed [1-10] [$player]"));
            return;
        }

        String speedArgument = args[0];

        try {
            byte speed = Byte.parseByte(speedArgument);

            if (speed <= 0 || speed > 10) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСкорость не должна быть меньше 0 и больше 10!"));
                return;
            }

            if (args.length == 1) {
                changeSpeedPlayer(p, speed);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСкорость " + (p.isFlying() ? "полета" : "ходьбы") + " изменена на &c" + speed));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null || !target.isOnline()) {
                p.sendMessage(DateMessage.PLAYER_NOT_FIND.getMessage(args[1]));
                return;
            }

            changeSpeedPlayer(target, speed);
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &c" + args[1] + " &fбыла изменена скорость на &a" + speedArgument));
        } catch (NumberFormatException e) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы должны ввести число!"));
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/speed &e$speed [1-10] [$player]"));
        }
    }

    @ConsoleExecute
    public void onConsoleExecutor(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

    private void changeSpeedPlayer(Player p, int speed) {
        final float result = 0.2f * speed;

        if (p.isFlying())
            p.setFlySpeed(result);
        else
            p.setWalkSpeed(result);
    }

}
