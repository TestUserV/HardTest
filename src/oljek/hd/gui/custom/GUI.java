package oljek.hd.gui.custom;

import com.oljek.main.util.StringUtil;
import oljek.hd.Hard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class GUI implements Listener {

    protected Inventory inv;
    protected Hard hard;
    protected Player p;

    public GUI(Hard hard, Player p) {
        this.hard = hard;
        this.p = p;

        inv = Bukkit.createInventory(null, getSlot(), StringUtil.inColor(getName()));

        Bukkit.getPluginManager().registerEvents(this, hard);
    }

    public abstract String getName();

    public abstract int getSlot();

    public abstract List<GUIItem> getItems();

    public void open() {
        inv.clear();

        setItems();

        p.openInventory(inv);
    }

    protected void setItems() {
        inv.clear();
        getItems().forEach((s) -> inv.setItem(s.getSlot(), s.getStack()));
    }

    public void close() {
        if (p != null && p.isOnline())
            p.closeInventory();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();

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

            List<GUIItem> items = getItems();

            ItemStack finalCurr = curr;
            items.forEach((s) -> {
                if (finalCurr.getType() == Material.AIR) {
                    if (s.isAir() && s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        return;
                    }
                } else {
                    if (s.getStack().equals(finalCurr) || s.getSlot() == e.getSlot()) {
                        s.getFunction().done(e);
                        return;
                    }
                }
            });

            if (curr.getType() == Material.AIR && cursor.getType() != Material.AIR) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            if (!this.p.equals(p))
                return;

            Inventory inv = e.getInventory();

            if (inv.equals(this.inv)) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(this.inv) && e.getPlayer().equals(this.p))
            HandlerList.unregisterAll(this);
    }

}
