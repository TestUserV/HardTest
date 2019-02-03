package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import oljek.hd.date.DeathDate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("back")
public class BackCommand {

    private Hard hard;

    public BackCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("back"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (!DeathDate.lastDeathLocation.containsKey(p.getUniqueId())) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fЛокация не найдена!"));
            return;
        }

        System.out.println("Test");

        p.teleport(DeathDate.lastDeathLocation.get(p.getUniqueId()));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
