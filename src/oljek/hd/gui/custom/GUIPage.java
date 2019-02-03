package oljek.hd.gui.custom;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.listener.custom.UpdateEvent;
import com.oljek.spigot.object.UpdateType;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class GUIPage extends GUI {

    protected int page;
    protected int cursorPage;
    private boolean started;
    private List<GUIItem> itemsUpdate;
    private boolean fill;
    private int cursor;

    public GUIPage(Hard hard, Player p, boolean fill) {
        super(hard, p);

        this.fill = fill;
        started = false;
        page = 1;
        itemsUpdate = new ArrayList<>();
        cursor = 0;
        cursorPage = 1;
    }

    public abstract List<GUIItem> getItemsAdditionally();

    public void open() {
        startTimer();
        p.openInventory(inv);
    }

    private void startTimer() {
        if (!started)
            started = true;
    }

    private void update() {
        if (inv == null)
            return;

        inv.clear();

        List<GUIItem> items = getItems();
        itemsUpdate = items;

        long countInPage = 0;

        do {
            if (page < 1) {
                p.closeInventory();
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСписок пуст!"));
                return;
            }

            countInPage = items.stream()
                    .filter((s) -> s.getPage() == this.page)
                    .count();

            if (countInPage < 1)
                page--;
        } while (countInPage < 1);

        List<GUIItem> itemInPage = items.stream()
                .filter((s) -> s.getPage() == this.page)
                .collect(Collectors.toList());

        if (itemInPage.stream().findAny().isPresent() && itemInPage.stream().findAny().get().getDate() != null)
            Collections.sort(itemInPage);

        AtomicInteger i = new AtomicInteger(10);

        itemInPage.stream()
                .forEach((s) -> {
                    switch (i.get()) {
                        case 17:
                            i.set(19);
                            break;
                        case 26:
                            i.set(28);
                            break;
                    }

                    if (i.get() <= 34) {
                        s.setSlot(i.get());
                        inv.setItem(s.getSlot(), s.getStack());

                        i.getAndIncrement();
                    }
                });

        List<GUIItem> additionallyItems = getItemsAdditionally();

        if (additionallyItems.size() != 0 && additionallyItems.size() <= 7) {
            if (additionallyItems.size() == 1)
                inv.setItem(49, additionallyItems.get(0).getStack());
            else {
                AtomicInteger a = new AtomicInteger(46);

                additionallyItems.forEach((s) -> {
                    System.out.println((a.get() - 45) - 1);
                    inv.setItem(a.getAndIncrement(), additionallyItems.get((a.get() - 45) - 2).getStack());
                });
            }
        }

        if (items.stream().filter(s -> s.getPage() >= this.page + 1).findAny().isPresent())
            inv.setItem(53, getNextPage().getStack());

        if (page > 1)
            inv.setItem(45, getPrevPage().getStack());

        if (itemInPage.size() < 21) {
            for (int j = 10; j <= 34; j++) {
                switch (j) {
                    case 17:
                        j = 19;
                        break;
                    case 26:
                        j = 28;
                        break;
                }

                if (inv.getItem(j) != null && inv.getItem(j).getType() != Material.AIR)
                    continue;

                inv.setItem(j, ItemUtil.create(Material.BARRIER)
                        .setName("&cОтсутствует.")
                        .getStack());
            }
        }

        if (fill) {
            for (int j = 0; j < inv.getSize(); j++) {
                if (inv.getItem(j) == null || inv.getItem(j).getType() == Material.AIR) {
                    inv.setItem(j, ItemUtil.create(Material.STAINED_GLASS_PANE)
                            .setDurabillity(10)
                            .setName("&5Светляшка!")
                            .getStack());
                }
            }
        }
        return;
    }

    private GUIItem getNextPage() {
        return new GUIItem(ItemUtil.create(Material.ARROW)
                .setName("&9Страница: &c" + (page + 1))
                .getStack())
                .setSlot(53)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    page++;
                });
    }

    private GUIItem getPrevPage() {
        return new GUIItem(ItemUtil.create(Material.ARROW)
                .setName("&9Страница: &c" + (page - 1))
                .getStack())
                .setSlot(45)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    page--;
                });
    }

    @Override
    public int getSlot() {
        return 54;
    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();

            if (p == null)
                return;

            if (!this.p.equals(p))
                return;

            if (inv == null)
                return;

            if (!inv.equals(this.inv))
                return;

            if (e.getClick().isShiftClick() || e.getClick() == ClickType.DOUBLE_CLICK) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                return;
            }

            ItemStack curr = e.getCurrentItem();
            ItemStack cursor = e.getCursor();

            if (curr == null)
                curr = new ItemStack(Material.AIR);

            if (cursor == null)
                cursor = new ItemStack(Material.AIR);

            List<GUIItem> items = itemsUpdate.stream().filter((s) -> s.getPage() == this.page).collect(Collectors.toList());

            ItemStack finalCurr = curr;
            items.forEach((s) -> {
                if (finalCurr.getType() == Material.AIR) {
                    if (s.isAir() && s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        update();
                        return;
                    }
                } else {
                    if (s.getStack().equals(finalCurr) || s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        update();
                        return;
                    }
                }
            });

            List<GUIItem> additionallyItems = getItemsAdditionally();

            additionallyItems.forEach((s) -> {
                if (finalCurr.getType() == Material.AIR) {
                    if (s.isAir() && s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        update();
                        return;
                    }
                } else {
                    if (s.getStack().equals(finalCurr) || s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        update();
                        return;
                    }
                }
            });

            if (e.getSlot() == 45 && inv.getItem(45) != null && inv.getItem(45).getType() != Material.AIR) {
                getPrevPage().getFunction().done(e);
                update();
                return;
            }

            if (e.getSlot() == 53 && inv.getItem(53) != null && inv.getItem(53).getType() != Material.AIR) {
                getNextPage().getFunction().done(e);
                update();
                return;
            }

            if (fill || curr.getType() == Material.BARRIER) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }

            if (curr.getType() == Material.AIR && cursor.getType() != Material.AIR) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.SECOND) {
            if (started)
                update();
        }
    }

    protected void checkPage() {
        if (cursor > 21) {
            cursor = 0;
            cursorPage++;
        }
    }

}
