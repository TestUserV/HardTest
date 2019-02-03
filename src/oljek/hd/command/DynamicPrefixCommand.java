package oljek.hd.command;

import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.object.Color;
import oljek.hd.object.Setting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

@Command({"DynamicPrefix", "df"})
public class DynamicPrefixCommand {

    private Hard hard;

    public DynamicPrefixCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        Setting setting = hard.getHardManager().getSetting(p.getName());

        Random rnd = new Random();
        Color random = Color.values()[new Random().nextInt(Color.values().length)];

        if (!setting.isEnableDynamicPrefix())
            setting.setEnableDynamicPrefix(true);
    }

    @ConsoleExecute
    public void onC(CommandSender sender, String[] args) {

    }

}
