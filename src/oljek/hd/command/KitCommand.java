package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.gui.KitAdminGUI;
import oljek.hd.gui.SelectTypeKitGUI;
import oljek.hd.gui.custom.GUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"kit", "k"})
public class KitCommand {

    private Hard hard;

    public KitCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (p.hasPermission(hard.getStorage().permissions.get("kit_managment"))) {
            GUI kitManagmentGUI = new KitAdminGUI(hard, p);
            kitManagmentGUI.open();
            return;
        }

        GUI selectTypeGUI = new SelectTypeKitGUI(hard, p);
        selectTypeGUI.open();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
