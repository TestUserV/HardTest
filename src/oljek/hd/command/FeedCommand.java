package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command("feed")
public class FeedCommand {

    private Hard hard;

    public FeedCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("feed"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы пополнили запас здоровья!"));
            return;
        }

        if (!p.hasPermission(hard.getStorage().permissions.get("feed_other"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            DateMessage.PLAYER_NOT_FIND.sendMessage(p, args[0]);
            return;
        }

        target.setFoodLevel(20);

        if (!target.equals(p))
            target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + p.getName() + " &fпополнил Вам запас еды!"));

        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы пополнили игроку: &c" + target.getName() + " &fзапас еды!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/feed $player");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Player: " + args[0] + " if offline!");
            return;
        }

        target.setFoodLevel(20);
        target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + sender.getName() + " &fпополнил Вам запас еды!"));
    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(hard.getStorage().permissions.get("feed_other")))
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
