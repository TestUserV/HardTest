package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListHisWarpPageGUI extends GUIPage {

    public ListHisWarpPageGUI(Hard hard, Player p) {
        super(hard, p, false);
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "&0Варпы > " + p.getName();
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();
        List<String> warps = Arrays.asList(hard.getHardManager().getAllForPlayer(p.getName(), true));

        int cursor = 0;
        int page = 1;

        for (String warp : warps) {
            if (cursor >= 21) {
                cursor = 0;
                page++;
            }

            ItemStack warpItem = ItemUtil.create(Material.COMPASS)
                    .setName("&a" + warp)
                    .addLore("&7>> Нажмите ПКМ что-бы удалить",
                            "&7>> Нажмите ЛКМ что-бы телепортироваться")
                    .getStack();

            items.add(new GUIItem(warpItem).setPage(page).setFunction((e) -> {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                if (e.getClick().isRightClick()) {
                    hard.getHardManager().delete(p.getName(), warp, true);
                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно удалили варп: &c" + warp));
                    return;
                }

                Location loc = hard.getHardManager().get(p.getName(), warp, true);

                p.teleport(loc);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортировались на варп: &c" + warp));
                return;
            }));
        }

        return items;
    }

}
