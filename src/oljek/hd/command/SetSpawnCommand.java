package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"sp", "setspawn"})
public class SetSpawnCommand {

    private Hard hard;

    public SetSpawnCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.isOp() && !p.hasPermission(hard.getStorage().permissions.get("setspawn"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        hard.getHardManager().setSpawn(p.getLocation());
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСпавн поставлен на локации:"));
        p.sendMessage(StringUtil.inColor(" &7>> &fМир: &c" + p.getLocation().getWorld().getName()));
        p.sendMessage(StringUtil.inColor(" &7>> &fX: &c" + p.getLocation().getX()));
        p.sendMessage(StringUtil.inColor(" &7>> &fY: &c" + p.getLocation().getY()));
        p.sendMessage(StringUtil.inColor(" &7>> &fZ: &c" + p.getLocation().getZ()));
        p.sendMessage(StringUtil.inColor(" &7>> &fYAW: &c" + p.getLocation().getYaw()));
        p.sendMessage(StringUtil.inColor(" &7>> &fPITCH: &c" + p.getLocation().getPitch()));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
