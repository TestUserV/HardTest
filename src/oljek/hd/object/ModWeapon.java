package oljek.hd.object;

import oljek.hd.Hard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ModWeapon implements Weapon, Listener {

    public ModWeapon() {
        Bukkit.getPluginManager().registerEvents(this, Hard.getInstance());

        new BukkitRunnable() {

            @Override
            public void run() {

            }

        }.runTaskTimer(Hard.getInstance(), 0, 20);
    }

    public abstract void onShot(PlayerInteractEvent e);

    public abstract void inHand(Player p);

    private void update() {

    }

}
