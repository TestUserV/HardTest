package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TakingDecisionGUI extends GUI {

    private Player target;
    private boolean decision;
    private long time;

    public TakingDecisionGUI(Hard hard, Player p, Player target) {
        super(hard, p);
        this.target = target;
        decision = false;
        time = System.currentTimeMillis() + (1000 * 60);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!decision) {
                    if (p != null)
                        p.closeInventory();
                }
            }

        }.runTaskLater(hard, 20 * 60);

        new BukkitRunnable() {

            @Override
            public void run() {
                if ((time - System.currentTimeMillis()) / 1000 <= 0) {
                    cancel();
                    p.closeInventory();
                    return;
                }

                setItems();
            }

        }.runTaskTimer(hard, 0, 20);
    }

    @Override
    public String getName() {
        return "&0Принятие решения";
    }

    @Override
    public int getSlot() {
        return 27;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        GUIItem infoItem = new GUIItem(ItemUtil.createHead(target.getName())
                .setName("&bИнформация")
                .addLore("",
                        " &7Игрок: &c" + target.getName() + " &7хочет",
                        " &7телепортироваться к Вам. &aПринять &7- он телепортируется,",
                        " &cОтклонить &7- он не телепортируется.",
                        " &7У Вас осталось на принятие решения: &a" + ((time - System.currentTimeMillis()) / 1000) + " &7сек.")
                .getStack())
                .setSlot(13)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                });

        GUIItem acceptItem = new GUIItem(ItemUtil.create(Material.WOOL)
                .setName("&aПринять")
                .setDurabillity(14)
                .getStack())
                .setSlot(11)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    decision = true;
                    p.closeInventory();

                    if (target == null) {
                        p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок вышел с сервера!"));
                        return;
                    }

                    target.teleport(p.getLocation());
                    target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы телепортированы к игроку: &c" + p.getName()));
                });

        GUIItem cancelItem = new GUIItem(ItemUtil.create(Material.WOOL)
                .setName("&cОтклонить")
                .setDurabillity(15)
                .getStack())
                .setSlot(15)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    decision = true;
                    p.closeInventory();

                    if (target != null) {
                        target.closeInventory();
                        target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок отклонил Вашу заявку!"));
                        return;
                    }

                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы отклонили заявку!"));
                });

        items.add(infoItem);
        items.add(acceptItem);
        items.add(cancelItem);

        return items;
    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);

        if (e.getInventory() != null && e.getInventory().equals(this.inv)) {
            if (!decision) {
                decision = true;

                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы не приняли решение!"));

                if (target != null)
                    target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + target.getName() + " &fне принял решение!"));
            }
        }
    }

}
