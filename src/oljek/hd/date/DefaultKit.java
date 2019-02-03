package oljek.hd.date;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DefaultKit {

    private transient static List<ItemStack> items;

    static {
        items = new ArrayList<>();
    }

    public static void load() {
        items.clear();

        items.add(ItemUtil.create(Material.STONE_SWORD)
                .setName("&aМеч от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.GOLD_AXE)
                .setName("&aТопор от OlJeK")
                .addLore("")
                .addEnchant(Enchantment.DURABILITY, 1)
                .getStack());

        items.add(ItemUtil.create(Material.WOOD_PICKAXE)
                .setName("&aКирка от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.WOOD_SPADE)
                .setName("&aЛопата от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.FISHING_ROD)
                .setName("&aУдочка от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.BOAT)
                .setName("&aЛодка от OlJeK")
                .addLore("")
                .getStack());

        ItemStack helmet = ItemUtil.create(Material.LEATHER_HELMET)
                .setName("&aШлем от OlJeK")
                .addLore("")
                .getStack();

        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();

        helmetMeta.setColor(values()[new Random().nextInt(values().length)]);
        helmet.setItemMeta(helmetMeta);

        items.add(helmet);

        items.add(ItemUtil.create(Material.GOLD_BOOTS)
                .setName("&aБотинки от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.LOG)
                .setName("&aДуб от OlJeK")
                .addLore("")
                .setCount(16)
                .getStack());

        items.add(ItemUtil.create(Material.TORCH)
                .setName("&aФакел от OlJeK")
                .addLore("")
                .setCount(16)
                .getStack());

        items.add(ItemUtil.create(Material.APPLE)
                .setName("&aЯблоко от OlJeK")
                .addLore("")
                .setCount(16)
                .getStack());

        items.add(ItemUtil.create(Material.BEETROOT)
                .setName("&aСвёкла от OlJeK")
                .addLore("")
                .setCount(16)
                .getStack());

        items.add(ItemUtil.create(Material.WORKBENCH)
                .setName("&aВерстак от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.FURNACE)
                .setName("&aПечка от OlJeK")
                .addLore("")
                .getStack());

        items.add(ItemUtil.create(Material.CHEST)
                .setName("&aСундук от OlJeK")
                .addLore("")
                .setCount(2)
                .getStack());


        items.add(ItemUtil.create(Material.COAL)
                .setName("&aУголь от OlJeK")
                .addLore("")
                .setCount(16)
                .getStack());

        items.add(ItemUtil.create(Material.BOAT)
                .setName("&aЛодка от OlJeK")
                .addLore("")
                .getStack());
    }

    public static List<ItemStack> getItems(String name) {
        return items.stream()
                .map((s) -> {
                    ItemStack cloned = s.clone();
                    ItemMeta meta = cloned.getItemMeta();
                    List<String> lore = meta.getLore();

                    lore.add(StringUtil.inColor(" &7> &fСпециально для: &6" + name + " "));

                    meta.setLore(lore);
                    cloned.setItemMeta(meta);
                    return cloned;
                }).collect(Collectors.toList());
    }

    private static Color[] values() {
        Color[] colors = new Color[18];

        colors[0] = Color.SILVER;
        colors[1] = Color.BLACK;
        colors[2] = Color.AQUA;
        colors[3] = Color.BLUE;
        colors[4] = Color.FUCHSIA;
        colors[5] = Color.GRAY;
        colors[7] = Color.GREEN;
        colors[8] = Color.LIME;
        colors[9] = Color.MAROON;
        colors[10] = Color.NAVY;
        colors[11] = Color.OLIVE;
        colors[12] = Color.ORANGE;
        colors[13] = Color.PURPLE;
        colors[14] = Color.RED;
        colors[15] = Color.TEAL;
        colors[16] = Color.WHITE;
        colors[17] = Color.YELLOW;

        return colors;
    }

}
