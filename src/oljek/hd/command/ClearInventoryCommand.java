package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"clearinventory", "cl", "clear"})
public class ClearInventoryCommand {

    private Hard hard;

    public ClearInventoryCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("cinventory"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        p.getInventory().clear();
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы очистили себе инвентарь!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
        return;
    }

}
