package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("help")
public class HelpCommand {

    private Hard hard;

    public HelpCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        hard.getStorage().helpMessages.forEach((s) -> p.sendMessage(StringUtil.inColor(s)));
    }

    @ConsoleExecute
    public void onConsoleExecite(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}