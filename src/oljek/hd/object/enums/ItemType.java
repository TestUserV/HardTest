package oljek.hd.object.enums;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public enum ItemType {

    ITEM, ENCHANT, POTION, SPAWN_EGG;

    public static ItemType getType(ConfigurationSection section) {
        if (!section.contains("itemType"))
            return ITEM;

        switch (section.getString("itemType").toUpperCase()) {
            case "ITEM":
                return ITEM;

            case "ENCHANT":
                return ENCHANT;

            case "POTION":
                return POTION;

            case "SPAWN_EGG":
                return SPAWN_EGG;

            default:
                return ITEM;
        }
    }

    public static ItemType getForItem(ItemStack stack) {
        if (stack.getType() == Material.POTION || stack.getType() == Material.SPLASH_POTION || stack.getType() == Material.LINGERING_POTION)
            return POTION;

        if (stack.getType() == Material.ENCHANTED_BOOK)
            return ENCHANT;

        if (stack.getType() == Material.MONSTER_EGG)
            return SPAWN_EGG;

        return ITEM;
    }

}
