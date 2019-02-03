package oljek.hd.command;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import oljek.hd.Hard;
import oljek.hd.api.manager.HardManager;
import oljek.hd.util.HomeUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command({"home", "h"})
public class HomeCommand {

    private Hard hard;

    public HomeCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (args.length < 1) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/h &e$name &7- телепортироваться в дом"));
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &f/h list &e$page &7- посмотреть список своих домов"));
            return;
        }

        HardManager hardManager = hard.getHardManager();
        String name = args[0];

        if (name.equals("list")) {
            int page = 0;
            int originalPage = 1;

            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                    originalPage = page;
                } catch (NumberFormatException e) {
                }
            }

            String[] homes = hardManager.getAllForPlayer(p.getName(), false);

            if (page > Math.ceil(homes.length / 10.0d)) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТакой страницы не существует!"));
                return;
            }

            if (homes.length < 1) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fУ Вас нет домов!"));
                return;
            }

            int resultAmountPage = 10 * (page == 0 ? 0 : (page - 1));

            int a = 0;

            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВсе Ваши дома:"));

            for (int i = resultAmountPage; i < homes.length; i++) {
                if (i >= resultAmountPage + 10)
                    break;

                if (homes.length - 1 < i)
                    break;

                TextComponent component = new TextComponent(homes[i]);
                HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Кликните что-бы телепортироваться"));
                ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/h " + homes[i]);
                component.setHoverEvent(he);
                component.setClickEvent(ce);

                TextComponent startComponent = new TextComponent(StringUtil.inColor(" &7" + ++a + ". - &f"));
                p.spigot().sendMessage(startComponent, component);
            }

            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСтраница: &c" + originalPage + "/" + (int) (homes.length < 10 ? 1 : Math.ceil(homes.length / 10.0d))));
            return;
        }

        Location home = hardManager.get(p.getName(), name, false);

        if (home == null) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТакой дом не найден!"));
            return;
        }

        p.teleport(home);
        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортированы в дом: &c" + name));
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Only for Player!");
    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        return HomeUtil.getTab(sender, args, hard);
    }

}
