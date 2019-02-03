package oljek.hd.object;

import org.bukkit.inventory.ItemStack;

public interface Weapon {

    ItemStack getItem();

    ItemStack getAmmo();

    double getDamage();

    double getCriticalDamage();

    double getChanceCriticalDamage();

}
