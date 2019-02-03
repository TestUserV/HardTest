package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import oljek.hd.date.DeathDate;
import oljek.hd.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command("god")
public class GodCommand {

    private Hard hard;

    public GodCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!hard.checkPermission(p, "god"))
            return;

        if (args.length == 0) {
            if (DeathDate.godList.contains(p.getUniqueId())) {
                DeathDate.godList.remove(p.getUniqueId());
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы выключили режим бога!"));
                return;
            }

            DeathDate.godList.add(p.getUniqueId());
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы включили режим бога!"));
            return;
        }

        if (!hard.checkPermission(p, "god_other"))
            return;

        if (!PlayerUtil.isOnline(args[0])) {
            DateMessage.PLAYER_NOT_FIND.sendMessage(p, args[0]);
            return;
        }

        Player target = PlayerUtil.getTarget(args[0]);

        if (DeathDate.godList.contains(target.getUniqueId())) {
            DeathDate.godList.remove(target.getUniqueId());
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы выключили режим бога игроку: &c" + target.getName()));
            return;
        }

        DeathDate.godList.add(target.getUniqueId());
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы включили режим бога игроку: &c" + target.getName()));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {

    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(hard.getStorage().permissions.get("god_other")))
            return new ArrayList<>();

        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream()
                    .map((s) -> s.getName())
                    .collect(Collectors.toList());
        } else if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter((s) -> s.getName().toLowerCase().contains(args[0].toLowerCase()))
                    .map((s) -> s.getName())
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

}
