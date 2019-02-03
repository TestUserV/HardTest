package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.ListPlayersTeleportPageGUI;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"call", "tpa"})
public class CallCommand {

    private Hard hard;

    public CallCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        GUIPage listPlayers = new ListPlayersTeleportPageGUI(hard, p, null);
        listPlayers.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
