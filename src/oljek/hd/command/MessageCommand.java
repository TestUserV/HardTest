package oljek.hd.command;

import com.google.common.collect.Lists;
import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command({"message", "mes", "msg", "m"})
public class MessageCommand {

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/m &e$player $message..."));
            return;
        }

        String target = args[0];
        Player onlinePlayer = Bukkit.getPlayer(target);

        if (onlinePlayer == null) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + target + " &fоффлайн!"));
            return;
        }

        if (p.getName().equalsIgnoreCase(target)) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fКхм.."));
            return;
        }

        String message = String.join(" ", Arrays.asList(args).stream().skip(1).collect(Collectors.toList()).toArray(new String[args.length - 1]));

        p.sendMessage(StringUtil.inColor("&f" + p.getName() + " &7>> &f" + target + "&e: " + message));
        onlinePlayer.sendMessage(StringUtil.inColor("&f" + target + " &7<< &f" + p.getName() + "&e: " + message));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("/m $player $message...");
            return;
        }

        String target = args[0];
        Player onlinePlayer = Bukkit.getPlayer(target);

        if (onlinePlayer == null) {
            sender.sendMessage("Player is offline!");
            return;
        }

        String message = String.join(" ", Arrays.asList(args).stream().skip(1).collect(Collectors.toList()).toArray(new String[args.length - 1]));

        sender.sendMessage(StringUtil.inColor("&f" + sender.getName() + " &7>> &f" + target + "&e: " + message));
        onlinePlayer.sendMessage(StringUtil.inColor("&f" + target + " &7<< &f" + sender.getName() + "&e: " + message));
    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            if (args.length == 0)
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            else
                return Bukkit.getOnlinePlayers().stream().filter((s) -> s.getName().toLowerCase().contains(args[0].toLowerCase())).map(HumanEntity::getName).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

}
