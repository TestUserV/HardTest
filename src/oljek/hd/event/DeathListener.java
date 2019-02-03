package oljek.hd.event;

import com.oljek.main.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import oljek.hd.Hard;
import oljek.hd.date.DeathDate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        DeathDate.lastDeathLocation.put(p.getUniqueId(), p.getLocation());

        new BukkitRunnable() {

            @Override
            public void run() {
                if (p != null && p.isDead())
                    p.spigot().respawn();
            }

        }.runTaskLater(Hard.getInstance(), 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (DeathDate.godList.contains(e.getEntity().getUniqueId()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Location spawnLocation = Hard.getInstance().getHardManager().getSpawn();

        if (spawnLocation == null && p.getBedSpawnLocation() == null) {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fСпавн временно не доступен!"));
            return;
        } else if (p.getBedSpawnLocation() != null) {
            e.setRespawnLocation(p.getBedSpawnLocation());
            return;
        }

        e.setRespawnLocation(spawnLocation);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player killer = (Player) e.getDamager();

            killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        }
    }

}
