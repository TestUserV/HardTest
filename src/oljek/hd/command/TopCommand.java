package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("top")
public class TopCommand {

    private Hard hard;

    public TopCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("top"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        Location pLocation = p.getLocation();
        World w = pLocation.getWorld();
        Location finalLocation = null;

        for (int y = pLocation.getBlockY(); y < 255; y++) {
            if (w.getBlockAt(new Location(w, pLocation.getX(), y, pLocation.getZ())).getType() == Material.AIR
                    && y + 1 <= 255 && w.getBlockAt(new Location(w, p.getLocation().getX(), y + 1, p.getLocation().getZ())).getType() == Material.AIR) {
                finalLocation = new Location(w, pLocation.getX(), y, pLocation.getZ());

                boolean fillAir = false;

                for (int y2 = finalLocation.getBlockY(); y2 < 255; y2++) {
                    if (w.getBlockAt(new Location(w, pLocation.getX(), y2, pLocation.getZ())).getType() != Material.AIR) {
                        fillAir = true;
                        break;
                    }
                }

                if (!fillAir)
                    break;
            }
        }

        if (finalLocation == null)
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТочки не найдено!"));

        p.teleport(finalLocation);
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортировались вверх!"));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
        return;
    }

}
