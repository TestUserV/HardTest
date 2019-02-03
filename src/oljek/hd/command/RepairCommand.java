package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("repair")
public class RepairCommand {

    private Hard hard;

    public RepairCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("repair"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        ItemStack inMainHand = p.getInventory().getItemInMainHand();

        if (inMainHand == null && inMainHand.getType() == Material.AIR) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВозьмите в руки предмет!"));
            return;
        }

        if (inMainHand.getType() == Material.FISHING_ROD ||
                inMainHand.getType().name().contains("SHOVEL") ||
                inMainHand.getType().name().contains("PICKAXE") ||
                inMainHand.getType().name().contains("AXE") ||
                inMainHand.getType() == Material.FLINT_AND_STEEL ||
                inMainHand.getType() == Material.SHEARS ||
                inMainHand.getType().name().contains("SWORD") ||
                inMainHand.getType() == Material.BOW ||
                inMainHand.getType().name().contains("HELMET") ||
                inMainHand.getType().name().contains("CHESTPLATE") ||
                inMainHand.getType().name().contains("LEGGINGS") ||
                inMainHand.getType().name().contains("BOOTS")) {

            inMainHand.setDurability(inMainHand.getType().getMaxDurability());

            System.out.println(inMainHand.getType().getMaxDurability() + "");

            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы починили предмет: &c" + inMainHand.getType().name()));
        } else {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fЭтот предмет нельзя починить!"));
            return;
        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
