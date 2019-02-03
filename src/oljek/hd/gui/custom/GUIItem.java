package oljek.hd.gui.custom;

import com.oljek.spigot.object.ItemFunction;
import com.oljek.spigot.object.OneDate;
import org.bukkit.inventory.ItemStack;

public class GUIItem implements Comparable {

    private ItemStack stack;
    private int slot;
    private ItemFunction function;
    private int page;
    private boolean isAir;
    private OneDate<Object, Comparable> date;

    public GUIItem(ItemStack stack) {
        this.stack = stack;

        page = 1;
    }

    public ItemStack getStack() {
        return stack;
    }

    public GUIItem setStack(ItemStack stack) {
        this.stack = stack;

        return this;
    }

    public int getSlot() {
        return slot;
    }

    public GUIItem setSlot(int slot) {
        this.slot = slot;

        return this;
    }

    public ItemFunction getFunction() {
        return function;
    }

    public GUIItem setFunction(ItemFunction function) {
        this.function = function;

        return this;
    }

    public boolean isAir() {
        return isAir;
    }

    public void setAir(boolean air) {
        isAir = air;
    }

    public int getPage() {
        return page;
    }

    public GUIItem setPage(int page) {
        this.page = page;

        return this;
    }

    public OneDate<Object, Comparable> getDate() {
        return date;
    }

    public GUIItem setDate(OneDate<Object, Comparable> date) {
        this.date = date;

        return this;
    }

    @Override
    public int compareTo(Object o) {
        if (date == null)
            return 0;

        return date.getVar2().compareTo(o);
    }

}
