package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import me.TechsCode.UltraPermissions.storage.objects.User;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.object.Kit;
import oljek.hd.object.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ListAllKitsPageGUI extends GUIPage {

    private boolean isWeek;

    public ListAllKitsPageGUI(Hard hard, Player p, boolean isWeek) {
        super(hard, p, false);
        this.isWeek = isWeek;
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "&0Выбор кита";
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();
        Kit[] kits = hard.getHardManager().getAllKits();
        User user = UltraPermissions.getAPI().getUsers().name(p.getName());
        Group defaultGroup = UltraPermissions.getAPI().getGroups().name("Default");
        Group group = (user == null ? defaultGroup : user.getGroups() == null ? defaultGroup : user.getGroups().get().length < 1 ? defaultGroup : user.getGroups().get()[0]);
        int pos = hard.getStorage().groupValue.getOrDefault(group.getName(), 1);

        for (Kit kit : kits) {
            if (kit.isWeak() != this.isWeek)
                continue;

            int posKit = hard.getStorage().groupValue.getOrDefault(kit.getDonateGroup(), 1);

            if (posKit > pos && !p.isOp())
                continue;

            KitPlayer kitPlayer = hard.getHardManager().getPlayerKit(p.getName());
            boolean canGive = kitPlayer.canGive(kit) || p.hasPermission(hard.getStorage().permissions.get("kit_give"));

            GUIItem kitItem = new GUIItem(ItemUtil.create(Material.STAINED_GLASS_PANE)
                    .setName((canGive ? "&a" : "&c") + kit.getName())
                    .setDurabillity((canGive ? 5 : 14))
                    .addLore((canGive ? "&7>> Нажмите ЛКМ что-бы получить" : "&cВам осталось подождать: &c" + (kitPlayer.remainedTimeGive(kit))),
                            (p.hasPermission(hard.getStorage().permissions.get("kit_delete")) ? "&7>> Нажмите ПКМ для действий" : ""))
                    .getStack())
                    .setPage(cursorPage)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);

                        if (e.getClick().isLeftClick()) {
                            if (!canGive) {
                                p.closeInventory();
                                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы не можете получить кит: &c" + kit.getName() + ". &fВам осталось подождать: &a" + (kitPlayer.remainedTimeGive(kit))));
                                return;
                            }

                            kitPlayer.give(kit);
                            p.closeInventory();
                            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы успешно взяли кит: &c" + kit.getName()));
                            return;
                        } else if (e.getClick().isRightClick()) {
                            if (!p.hasPermission(hard.getStorage().permissions.get("kit_delete")))
                                return;

                            GUI kitManagmentGUI = new KitManagmentGUI(hard, p, kit);
                            kitManagmentGUI.open();
                            return;
                        }
                    });

            items.add(kitItem);

            checkPage();
        }

        return items;
    }

}
