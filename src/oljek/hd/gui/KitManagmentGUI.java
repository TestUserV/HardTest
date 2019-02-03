package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.object.AnvilGUI;
import com.oljek.spigot.util.ItemUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.object.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitManagmentGUI extends GUI {

    private Kit kit;

    public KitManagmentGUI(Hard hard, Player p, Kit kit) {
        super(hard, p);
        this.kit = kit;
    }

    @Override
    public String getName() {
        return "&0Управление китом";
    }

    @Override
    public int getSlot() {
        return 36;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        GUIItem saveItem = new GUIItem(ItemUtil.create(Material.STAINED_GLASS_PANE)
                .setDurabillity(6)
                .setName("&aСохранить инвентарь")
                .getStack())
                .setSlot(12)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    kit.clear();

                    for (int i = 0; i < p.getInventory().getSize(); i++) {
                        ItemStack stack = p.getInventory().getItem(i);

                        if (stack == null || stack.getType() == Material.AIR)
                            continue;

                        kit.setItem(i, stack);
                    }

                    kit.save();

                    p.closeInventory();
                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно изменили инвентарь для кита: &c" + kit.getName()));
                });

        GUIItem deleteItem = new GUIItem(ItemUtil.create(Material.BARRIER)
                .setName("&cУдалить кит")
                .getStack())
                .setSlot(13)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    hard.getHardManager().deleteKit(kit.getName());
                    p.closeInventory();
                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно удалили кит: &c" + kit.getName()));
                });

        GUIItem changeDonateGroupItem = new GUIItem(ItemUtil.create(Material.EMERALD)
                .setName("&6Изменить донат группу")
                .getStack())
                .setSlot(14)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    new AnvilGUI(hard, p, "Название донат группы", (p2, s) -> {
                        Group group = UltraPermissions.getAPI().getGroups().name(s);

                        if (group == null) {
                            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fГруппа: &c" + s + " &fне найдена!"));
                            return s;
                        }

                        p.closeInventory();
                        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы изменили группу кита: &c" + kit.getName() + " &fна: &a" + s));
                        return s;
                    });
                });

        GUIItem changePeriodItem = new GUIItem(ItemUtil.create(Material.WATCH)
                .setName("&bИзменить период")
                .getStack())
                .setSlot(22)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

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

                                        kit.setWeak(true);
                                        kit.save();
                                        p.closeInventory();
                                        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно изменили период кита: &c" + kit.getName() + " &fна: &aнеделью"));
                                    });

                            GUIItem itemMonth = new GUIItem(ItemUtil.create(Material.GOLD_INGOT)
                                    .setName("&cЕжемесячный")
                                    .getStack())
                                    .setSlot(14)
                                    .setFunction((e) -> {
                                        e.setCancelled(true);
                                        e.setResult(Event.Result.DENY);

                                        kit.setWeak(false);
                                        kit.save();
                                        p.closeInventory();
                                        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно изменили период кита: &c" + kit.getName() + " &fна: &aмесяц"));
                                    });

                            items.add(itemWeek);
                            items.add(itemMonth);
                            return items;
                        }

                    }.open();
                });

        items.add(saveItem);
        items.add(deleteItem);
        items.add(changePeriodItem);
        items.add(changeDonateGroupItem);
        return items;
    }

}
