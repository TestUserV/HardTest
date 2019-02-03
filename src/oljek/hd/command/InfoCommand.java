package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.InfoGUI;
import oljek.hd.gui.custom.GUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"info", "i"})
public class InfoCommand {

    private Hard hard;

    public InfoCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        GUI gui = new InfoGUI(hard, p);
        gui.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
