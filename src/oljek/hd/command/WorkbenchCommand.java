package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("workbench")
public class WorkbenchCommand {

    private Hard hard;

    public WorkbenchCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("workbench"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        p.openWorkbench(null, true);
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender) {
        sender.sendMessage("Only for Players!");
        return;
    }

}
