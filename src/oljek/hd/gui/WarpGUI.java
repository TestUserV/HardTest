package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.object.AnvilGUI;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.object.enums.ResultOperation;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WarpGUI extends GUI {

    public WarpGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Варпы";
    }

    @Override
    public int getSlot() {
        return 27;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        ItemStack listHisWarpsItem = ItemUtil.createHead(p.getName())
                .setName("&aПосмотреть список своих варпов")
                .getStack();

        items.add(new GUIItem(listHisWarpsItem)
                .setSlot(12)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUIPage pageGUI = new ListHisWarpPageGUI(hard, p);
                    pageGUI.open();
                }));

        ItemStack createWarpItem = ItemUtil.create(Material.NAME_TAG)
                .setName("&6Создать варп")
                .getStack();

        items.add(new GUIItem(createWarpItem)
                .setSlot(13)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    new AnvilGUI(hard, p, "Введите название", (p1, s) -> {
                        ResultOperation operation = hard.getHardManager().set(p.getName(), s, p.getLocation(), true);

                        switch (operation) {
                            case NAME_CONTAINS: {
                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТакое имя варпа уже существует!"));
                                break;
                            }

                            case ADD: {
                                p.closeInventory();
                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВарп: &c" + s + " &fуспешно был создан!"));
                                p.sendTitle(StringUtil.inColor("&9Варп: &c" + s), StringUtil.inColor("&cуспешно был создан!"), 80, 120, 20);
                                p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 0, 5);

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        GUI gui = new WarpGUI(hard, p);
                                        gui.open();
                                    }

                                }.runTaskLater(hard, 120);
                                break;
                            }
                        }
                        return s;
                    });
                }));

        ItemStack listWarpsItem = ItemUtil.createHead("OlJeK")
                .setName("&cПосмотреть список всех варпов")
                .getStack();

        items.add(new GUIItem(listWarpsItem)
                .setSlot(14)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUIPage pageGUI = new ListWarpsPageGUI(hard, p, null);
                    pageGUI.open();
                }));

        return items;
    }

}
