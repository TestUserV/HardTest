package oljek.hd.gui;

import com.google.common.collect.Lists;
import com.oljek.spigot.manager.ConfigManager;
import com.oljek.spigot.object.AnvilGUI;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TestPageGui extends GUIPage {

    private String search;

    public TestPageGui(Hard hard, Player p, String search) {
        super(hard, p, true);

        this.search = search;
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        GUIItem item = new GUIItem(ItemUtil.create(Material.COMPASS).setName("Поиск").getStack()).setFunction(e -> {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);

            new AnvilGUI(hard, p, "Введите слово", (player, s) -> {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        GUIPage gui = new TestPageGui(Hard.getInstance(), player, s);
                        gui.open();
                    }

                }.runTaskLater(hard, 20);
                return s;
            });
        });

        return Lists.newArrayList(item);
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public List<GUIItem> getItems() {
        ConfigManager cfg = new ConfigManager(hard, "testItems");
        cfg.create();

        List<GUIItem> item = new ArrayList<>();
        int count = 0;
        int page = 1;

        for (String s : cfg.getConfiguration().getConfigurationSection("").getKeys(false)) {
            if (search != null && !search.isEmpty()) {
                if (!s.contains(search))
                    continue;
            }

            if (count >= 21) {
                count = 0;
                page++;
            }

            item.add(new GUIItem(ItemUtil.createHead(s).setName("&a" + s).getStack()).setPage(page).setFunction((e) -> {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                p.sendMessage(s);
                p.closeInventory();
                return;
            }));

            count++;
        }

        return item;
    }

}
