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

@Command({"suicide", "sc"})
public class SuicideCommand {

    private Hard hard;

    public SuicideCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("suicide"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        p.setHealth(0);
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fШалун :)"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/sc $player");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Player: " + args[0] + " is offline!");
            return;
        }

        target.setHealth(0);
    }

}
