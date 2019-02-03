package oljek.hd.object;

import com.oljek.spigot.manager.ConfigManager;
import com.oljek.spigot.object.ConfigKit;
import com.oljek.spigot.object.OneDate;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit extends ConfigKit {

    private String donateGroup;
    private boolean isWeak;

    public Kit(String name, ConfigManager cfg) {
        super(name, cfg);
        this.name = name;

        load();
    }

    protected void load() {
        super.load();

        if (cfg.getConfiguration().contains("temp.isWeak"))
            isWeak = cfg.getConfiguration().getBoolean("temp.isWeak");

        if (cfg.getConfiguration().contains("temp.donateGroup"))
            donateGroup = cfg.getConfiguration().getString("temp.donateGroup");
    }

    public ItemStack getItem(int slot) {
        return items.stream().filter((s) -> s.getVar1() == slot).map((s) -> s.getVar2()).findAny().get();
    }

    public List<OneDate<Integer, ItemStack>> getItems() {
        return items;
    }

    public void setItems(List<OneDate<Integer, ItemStack>> items) {
        this.items = items;
    }

    public String getDonateGroup() {
        return donateGroup;
    }

    public void setDonateGroup(String donateGroup) {
        cfg.set("temp.donateGroup", donateGroup);
        cfg.save();

        this.donateGroup = donateGroup;
    }

    public boolean isWeak() {
        return isWeak;
    }

    public void setWeak(boolean weak) {
        cfg.set("temp.isWeak", weak);
        cfg.save();

        isWeak = weak;
    }

    public String getName() {
        return name;
    }

    public void setItem(int slot, ItemStack stack) {
        items.add(new OneDate<>(slot, stack));
    }

    public void save() {
        super.save();

        cfg.set("temp.donateGroup", donateGroup);
        cfg.set("temp.isWeak", isWeak);
        cfg.save();
    }

}
