package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import oljek.hd.Hard;
import oljek.hd.util.HomeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command({"deletehome", "dh"})
public class DeleteHomeCommand {

    private Hard hard;

    public DeleteHomeCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (args.length == 0) {

        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {

    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        return HomeUtil.getTab(sender, args, hard);
    }

}
