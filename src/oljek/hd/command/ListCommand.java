package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.ListPlayersPageGUI;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"list", "l"})
public class ListCommand {

    private Hard hard;

    public ListCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        GUIPage listGUI = new ListPlayersPageGUI(hard, p);
        listGUI.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
