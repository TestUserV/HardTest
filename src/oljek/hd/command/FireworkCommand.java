package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

@Command("firework")
public class FireworkCommand {

    private Hard hard;

    public FireworkCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("firework"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
        firework.detonate();
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
