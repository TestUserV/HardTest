package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.GeneralGUI;
import oljek.hd.gui.custom.GUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"general", "main", "g", "главная", "меню"})
public class GeneralCommand {

    private Hard hard;

    public GeneralCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        GUI generalGUI = new GeneralGUI(hard, p);
        generalGUI.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
