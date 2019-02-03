package oljek.hd.gui;

import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class SelectTypeKitGUI extends GUI {

    public SelectTypeKitGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Выбор типа кита";
    }

    @Override
    public int getSlot() {
        return 27;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>(2);

        GUIItem itemWeek = new GUIItem(ItemUtil.create(Material.IRON_INGOT)
                .setName("&aЕженедельный")
                .getStack())
                .setSlot(12)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUIPage listAllKitsGUI = new ListAllKitsPageGUI(hard, p, true);
                    listAllKitsGUI.open();
                });

        GUIItem itemMonth = new GUIItem(ItemUtil.create(Material.GOLD_INGOT)
                .setName("&cЕжемесячный")
                .getStack())
                .setSlot(14)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUIPage listAllKitsGUI = new ListAllKitsPageGUI(hard, p, false);
                    listAllKitsGUI.open();
                });

        items.add(itemWeek);
        items.add(itemMonth);
        return items;
    }

}
