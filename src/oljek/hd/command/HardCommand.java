package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.config.Storage;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("hard")
public class HardCommand {

    private Hard hard;

    public HardCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("hard"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/hard reload &7- перезагрузить конфиг"));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reload();
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fКонфиг перезагружен!"));
            return;
        }

        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fАргумент: &c" + args[0] + " &fне найден!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        reload();
        sender.sendMessage("Config reloaded");
    }

    private void reload() {
        Storage.loadPlugin(hard);
        hard.reloadAnimateStorage();
    }

}
