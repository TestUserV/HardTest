package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.object.AnvilGUI;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.object.Warp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListWarpsPageGUI extends GUIPage {

    private String search;

    public ListWarpsPageGUI(Hard hard, Player p, String search) {
        super(hard, p, false);

        this.search = search;
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        List<GUIItem> items = new ArrayList<>(2);

        items.add(new GUIItem(ItemUtil.create(Material.COMPASS)
                .setName("&9Поиск")
                .getStack()).setFunction((e) -> {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);

            new AnvilGUI(hard, p, "Введите название", (p1, s) -> {
                GUIPage guiPage = new ListWarpsPageGUI(hard, p, s);
                guiPage.open();
                return s;
            });
        }));

        if (search != null && !search.isEmpty()) {
            items.add(new GUIItem(ItemUtil.create(Material.BARRIER)
                    .setName("&cУбрать поиск")
                    .getStack()).setFunction((e) -> {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                GUIPage guiPage = new ListWarpsPageGUI(hard, p, null);
                guiPage.open();
            }));
        }

        return items;
    }

    @Override
    public String getName() {
        return "&0Варпы > Все варпы";
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();
        List<Warp> warps = Arrays.asList(hard.getHardManager().getAll(true));

        if (search != null && !search.isEmpty())
            warps = warps.stream().filter((s) -> s.getName().contains(search)).collect(Collectors.toList());

        int cursor = 0;
        int page = 1;

        for (Warp warp : warps) {
            if (cursor >= 21) {
                cursor = 0;
                page++;
            }

            ItemStack warpItem;

            if (p.hasPermission(hard.getStorage().permissions.get("warp.delete.other"))) {
                warpItem = ItemUtil.create(Material.COMPASS)
                        .setName("&a" + warp.getName())
                        .addLore("&7>> Нажмите ПКМ что-бы удалить",
                                "&7>> Нажмите ЛКМ что-бы телепортироваться",
                                "",
                                "&fВладелец: &c" + warp.getOwner())
                        .getStack();
            } else {
                warpItem = ItemUtil.create(Material.COMPASS)
                        .setName("&a" + warp.getName())
                        .addLore("&7>> Нажмите ЛКМ что-бы телепортироваться",
                                "",
                                "&fВладелец: &c" + warp.getOwner())
                        .getStack();
            }

            items.add(new GUIItem(warpItem).setPage(page).setFunction((e) -> {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                if (e.getClick().isRightClick()) {
                    if (!p.hasPermission(hard.getStorage().permissions.get("warp.delete.other")))
                        return;

                    hard.getHardManager().delete(p.getName(), warp.getName(), true);
                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно удалили варп: &c" + warp.getName()));
                    return;
                }

                Location loc = warp.getLoc();

                p.teleport(loc);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортировались на варп: &c" + warp.getName()));
                return;
            }));
        }

        return items;
    }

}
