package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.ListWarpsPageGUI;
import oljek.hd.gui.WarpGUI;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"warp", "w"})
public class WarpCommand {

    private Hard hard;

    public WarpCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("warp"))) {
            GUIPage pageGUI = new ListWarpsPageGUI(hard, p, null);
            pageGUI.open();
            return;
        }

        GUI warpGui = new WarpGUI(hard, p);
        warpGui.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
