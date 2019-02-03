package oljek.hd.object;

import com.oljek.spigot.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class M4A1 extends ModWeapon {

    @Override
    public void onShot(PlayerInteractEvent e) {

    }

    @Override
    public void inHand(Player p) {

    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.WOOD_PICKAXE)
                .setName("&aM4A1")
                .addLore("",
                        "&fУрон &c" + getDamage())
                .getStack();
    }

    @Override
    public ItemStack getAmmo() {
        return ItemUtil.create(Material.STICK)
                .setName("Патроны STICK")
                .getStack();
    }

    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public double getCriticalDamage() {
        return 15;
    }

    @Override
    public double getChanceCriticalDamage() {
        return 50;
    }

}
