package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("spawn")
public class SpawnCommand {

    private Hard hard;

    public SpawnCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (hard.getHardManager().getSpawn() == null) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСпавн не установлен!"));
            return;
        }

        p.teleport(hard.getHardManager().getSpawn());
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы были телепортированы на спавн!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/spawn $player");
            return;
        }

        String target = args[0];
        Player onlineTarget = Bukkit.getPlayer(target);

        if (onlineTarget == null) {
            sender.sendMessage("Player not found!");
            return;
        }

        if (hard.getHardManager().getSpawn() == null) {
            sender.sendMessage("Spawn not set!");
            return;
        }

        onlineTarget.teleport(hard.getHardManager().getSpawn());
        sender.sendMessage("Player: " + onlineTarget.getName() + " teleported to spawn!");
    }

}
