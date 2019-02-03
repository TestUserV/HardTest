package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.FastMessagePageGUI;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"fastmessage", "fm"})
public class FastMessageCommand {

    private Hard hard;

    public FastMessageCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!hard.checkPermission(p, "fastmessage"))
            return;

        GUIPage fastMessageGUI = new FastMessagePageGUI(hard, p);
        fastMessageGUI.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
