package oljek.hd.gui;

import com.oljek.spigot.MainAPISpigot;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.storage.ConfigStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfoGUI extends GUI {

    public InfoGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Информация > &c&lHARD";
    }

    @Override
    public int getSlot() {
        return 9;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>(1);

        ItemStack bankInfo = ItemUtil.create(Material.GOLD_INGOT)
                .setName(MainAPISpigot.getClass(hard, ConfigStorage.class).guiInfoBankName)
                .setLore(MainAPISpigot.getClass(hard, ConfigStorage.class).guiInfoBankLore)
                .getStack();

        items.add(new GUIItem(bankInfo).setFunction(e -> {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }));

        return items;
    }

}
