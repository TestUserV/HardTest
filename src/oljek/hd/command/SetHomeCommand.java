package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import oljek.hd.Hard;
import oljek.hd.api.manager.HardManager;
import oljek.hd.object.enums.ResultOperation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"sethome", "sh"})
public class SetHomeCommand {

    public static final String PATTERN_SET_HOME = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
    private Hard hard;

    public SetHomeCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/sh &e$name &7- создать дом"));
            return;
        }

        String name = args[0];
        HardManager hardManager = hard.getHardManager();
        User user = UltraPermissions.getAPI().getUsers().name(p.getName());
        String group = "Default";

        if (user != null && user.getGroups() != null && user.getGroups().get().length > 0)
            group = user.getGroups().get()[0].getName();

        int limitHouse = (hard.getStorage().maxAmountHomes.containsKey(group) ? hard.getStorage().maxAmountHomes.get(group) : 1);
        int currentHouses = hardManager.amount(p.getName(), false);

        if (currentHouses >= limitHouse) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fУ Вас закончился лимит домов! На данные момент он состовляет: &c" + limitHouse));
            return;
        }

        if (name.equals("list")) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fЭто имя нельзя поставить! Придумайте другое."));
            return;
        }

        char[] chars = name.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (!PATTERN_SET_HOME.contains(String.valueOf(c))) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИспользуйте только цифры и английские символы!"));
                return;
            }
        }

        ResultOperation result = hardManager.set(p.getName(), name, p.getLocation(), false);

        switch (result) {
            case NAME_CONTAINS: {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИмя: &c" + name + " &fуже используеться Вами!"));
                return;
            }

            case ADD:
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно добавили дом под навзанием: &c" + name));
        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Players!");
    }

}
