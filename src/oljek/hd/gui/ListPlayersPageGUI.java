package oljek.hd.gui;

import com.oljek.spigot.object.OneDate;
import com.oljek.spigot.util.ItemUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.object.PlayerComparable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ListPlayersPageGUI extends GUIPage {

    public ListPlayersPageGUI(Hard hard, Player p) {
        super(hard, p, false);
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "&0Список всех игроков";
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        int cursor = 0;
        int page = 1;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (cursor > 21) {
                cursor = 0;
                page++;
            }
            User user = UltraPermissions.getAPI().getUsers().name(player.getName());
            String group = "&c&lНЕТ";
            String prefix = "&c&lНЕТ";

            if (user != null && user.getGroups() != null && user.getGroups().get().length > 0)
                group = user.getGroups().get()[0].getName();

            if (user != null) {
                if (user.getPrefix() != null && !user.getPrefix().isEmpty())
                    prefix = user.getPrefix();
                else if (!group.equals("&c&lНЕТ"))
                    prefix = user.getGroups().name(group).getPrefix();
            }

            PlayerComparable comparable = new PlayerComparable(user, hard);
            OneDate<Object, Comparable> date = new OneDate<>(comparable, comparable);

            GUIItem item = new GUIItem(ItemUtil.createHead(player.getName())
                    .setName("&a" + player.getName())
                    .addLore("",
                            "&fГруппа: &c" + group,
                            "&fПрефикс: &c" + prefix)
                    .getStack())
                    .setPage(page)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    })
                    .setDate(date);

            items.add(item);
        }

        return items;
    }

}
