package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("seen")
public class SeenCommand {

    @PlayerExecute
    public void onTest(Player p, String[] args) {
        p.sendMessage(Hard.getInstance().getHardManager().getIPAddress(args[0]));
    }

    @ConsoleExecute
    public void onConsoleTest(CommandSender sender, String[] args) {

    }

}
