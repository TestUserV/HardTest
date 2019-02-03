package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import oljek.hd.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command("ext")
public class ExtCommand {

    private Hard hard;

    public ExtCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("ext"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.setFireTicks(0);
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы потушили себя!"));
            return;
        }

        if (!p.hasPermission(hard.getStorage().permissions.get("ext_other"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (!PlayerUtil.isOnline(args[0])) {
            DateMessage.PLAYER_NOT_FIND.sendMessage(p, args[0]);
            return;
        }

        Player target = PlayerUtil.getTarget(args[0]);

        target.setFireTicks(0);

        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &a" + args[0] + " &fпотушен!"));
        target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &a" + p.getName() + " &fпотушил Вас!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
        return;
    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(hard.getStorage().permissions.get("ext_other")))
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
