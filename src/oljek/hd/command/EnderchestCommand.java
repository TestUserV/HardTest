package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"enderchest", "ec"})
public class EnderchestCommand {

    private Hard hard;

    public EnderchestCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("enderchest"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        p.openInventory(p.getEnderChest());
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
