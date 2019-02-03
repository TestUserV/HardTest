package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.object.AnvilGUI;
import com.oljek.spigot.util.ItemUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import oljek.hd.Hard;
import oljek.hd.api.manager.HardManager;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class KitAdminGUI extends GUI {

    public KitAdminGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Управление китами";
    }

    @Override
    public int getSlot() {
        return 27;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        // create
        // list

        GUIItem createItem = new GUIItem(ItemUtil.create(Material.NAME_TAG)
                .setName("&aСоздать кит")
                .getStack())
                .setSlot(12)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    HardManager hardManager = hard.getHardManager();

                    new AnvilGUI(hard, p, "Название кита", (p1, s) -> {
                        if (hardManager.getKit(s) != null) {
                            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТакой кит уже существует!"));
                            return s;
                        }

                        new AnvilGUI(hard, p, "Название донат группы", (p2, s2) -> {
                            Group group = UltraPermissions.getAPI().getGroups().name(s2);

                            if (group == null) {
                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fГруппа: &c" + s2 + " &fне найдена!"));
                                return s2;
                            }

                            new GUI(hard, p) {

                                @Override
                                public String getName() {
                                    return "&0Управление китами";
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

                                                hardManager.createKit(s, group.getName(), true);
                                                p.closeInventory();
                                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно создали кит: &c" + s));
                                            });

                                    GUIItem itemMonth = new GUIItem(ItemUtil.create(Material.GOLD_INGOT)
                                            .setName("&cЕжемесячный")
                                            .getStack())
                                            .setSlot(14)
                                            .setFunction((e) -> {
                                                e.setCancelled(true);
                                                e.setResult(Event.Result.DENY);

                                                hardManager.createKit(s, group.getName(), false);
                                                p.closeInventory();
                                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно создали кит: &c" + s));
                                            });

                                    items.add(itemWeek);
                                    items.add(itemMonth);
                                    return items;
                                }

                            }.open();
                            return s2;
                        });
                        return s;
                    });
                });

        GUIItem listItem = new GUIItem(ItemUtil.create(Material.BOOK)
                .setName("&cПосмотреть список всех китов")
                .getStack())
                .setSlot(14)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUI selectType = new SelectTypeKitGUI(hard, p);
                    selectType.open();
                });

        items.add(createItem);
        items.add(listItem);

        return items;
    }

}
