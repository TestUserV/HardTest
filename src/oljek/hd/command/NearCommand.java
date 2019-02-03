package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("near")
public class NearCommand {

    private Hard hard;

    public NearCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("near"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/near &e$radius &7- посмотреть игроков в радиусе"));
            return;
        }

        try {
            int radius = Integer.parseInt(args[0]);

            if (radius <= 0) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fРадиус должен быть больше нуля!"));
                return;
            }

            if (radius >= 1000) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fРадиус должен быть меньше тысячи!"));
                return;
            }

            StringBuilder builder = new StringBuilder(StringUtil.inColor("&c&lHARD &7>> &fСписок игроков в радиусе &c" + radius + ": &6"));

            int count = 0;

            for (Player player : p.getWorld().getPlayers()) {
                if (player.equals(p))
                    continue;

                long distance = (long) player.getLocation().distance(p.getLocation());

                if (distance <= radius) {
                    if (count == 0) {
                        builder.append(player.getName()).append(" ").append("§7(").append("§e").append(distance).append("§7)");
                        count++;
                        continue;
                    }

                    builder.append("§f, §6").append(player.getName()).append(" ").append("§7(").append("§e").append(distance).append("§7)");
                    count++;
                }
            }

            if (count == 0) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fНа данный момент в таком радиусе игроков нет!"));
                return;
            }

            p.sendMessage(builder.toString());
        } catch (NumberFormatException e) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИспользуйте цифру в качестве аргумента 1"));
            return;
        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
