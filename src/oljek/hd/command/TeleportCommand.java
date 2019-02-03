package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"teleport", "tp"})
public class TeleportCommand {

    private Hard hard;

    public TeleportCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!hard.checkPermission(p, "tp"))
            return;

        if (args.length == 0) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/tp &e$player [player to]"));
            return;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null || !target.isOnline()) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + args[0] + " &fне найден!"));
                return;
            }

            if (p.equals(target)) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fОдиночество..."));
                return;
            }

            p.teleport(target);
            return;
        }

        Player playerFrom = Bukkit.getPlayer(args[0]);
        Player playerTo = Bukkit.getPlayer(args[1]);

        if (playerFrom == null || playerTo == null || !playerFrom.isOnline() || !playerTo.isOnline()) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fКакой-то из игроков на данный момент оффлайн!"));
            return;
        }

        if (playerFrom.equals(p) && playerTo.equals(p)) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fОдиночество..."));
            return;
        }

        playerFrom.teleport(playerTo);
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортировали: &c" + playerFrom.getName() + " &fк: &c" + playerTo.getName()));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("no no no");
    }

}
