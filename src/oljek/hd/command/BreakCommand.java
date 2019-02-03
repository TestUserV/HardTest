package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

@Command("break")
public class BreakCommand {

    private Hard hard;

    public BreakCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        BlockIterator blockIterator = new BlockIterator(p.getLocation(), 0, 50);

        Block bl = null;

        while (blockIterator.hasNext()) {
            if (bl != null && bl.getType() != Material.AIR)
                break;

            bl = blockIterator.next();
        }

        if (bl == null) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСпереди в радиусе 50 блоков нету блоков!"));
            return;
        }

        bl.setType(Material.AIR);
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
