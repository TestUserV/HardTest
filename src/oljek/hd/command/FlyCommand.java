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

@Command("fly")
public class FlyCommand {

    private Hard hard;

    public FlyCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("fly"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            p.setAllowFlight(!p.getAllowFlight());
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы " + (p.getAllowFlight() ? "&aвключили" : "&cвыключили") + " &fрежим полета."));
            return;
        }

        if (!p.hasPermission(hard.getStorage().permissions.get("fly_other"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            DateMessage.PLAYER_NOT_FIND.sendMessage(p, args[0]);
            return;
        }

        target.setAllowFlight(!p.getAllowFlight());
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы " + (p.getAllowFlight() ? "&aвключили" : "&cвыключили") + " &fигроку: &c" + p.getName() + " &fрежим полета."));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players");
        return;
    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(hard.getStorage().permissions.get("fly_other")))
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
