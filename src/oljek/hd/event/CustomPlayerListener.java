package oljek.hd.event;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.listener.custom.UpdateEvent;
import com.oljek.spigot.object.UpdateType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import oljek.hd.Hard;
import oljek.hd.object.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class CustomPlayerListener implements Listener {

    private static final String[] lines = new String[]{
            "&c&l&nO&f",
            "&4&l&nl&f",
            "&c&l&nJ&f",
            "&4&l&ne&f",
            "&c&l&nK&f"
    };
    private Player p;
    private Hard hard;
    private boolean isSendDamage;
    private long lastUpdate;
    private double finalDamage;
    private double fullHp;
    private String entityName;
    private int cursor;

    public CustomPlayerListener(Player p, Hard hard) {
        this.p = p;
        this.hard = hard;
        entityName = "";
        finalDamage = 0;

        Bukkit.getPluginManager().registerEvents(this, hard);
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.SECOND) {
            if (p == null) {
                HandlerList.unregisterAll(this);
                return;
            }

            if (!isSendDamage) {
                if (cursor >= lines.length)
                    cursor = 0;

                String line = lines[cursor];

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringUtil.inColor(line + " &fУровень &c1, &fпрогресс &a&n■■■■■■■■■■&c■■■■■ &680% / 100% " + line)));
                cursor++;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(hard, () -> {
            Setting setting = hard.getHardManager().getSetting(p.getName());

            if (setting.isEnableBlood()) {
                Location loc = e.getEntity().getLocation();
                PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(EnumParticle.DAMAGE_INDICATOR, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(),
                        0F, 0F, 0F, 1.0F, 10, CraftParticle.toData(Particle.DAMAGE_INDICATOR, null));

                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(particlePacket);
            }
        });

        if (e.getDamager() instanceof Player && e.getDamager().equals(p)) {
            finalDamage = e.getFinalDamage();
            entityName = e.getEntity().getName();
            lastUpdate = System.currentTimeMillis();

            LivingEntity livingEntity = (LivingEntity) e.getEntity();

            fullHp = livingEntity.getHealth() - e.getFinalDamage();
            fullHp = Math.max(fullHp, 0);

            if (!isSendDamage) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!isSendDamage)
                            isSendDamage = true;

                        if (System.currentTimeMillis() - lastUpdate >= 3000L) {
                            cancel();
                            isSendDamage = false;
                            return;
                        }

                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringUtil.inColor("&fВы нанесли урон существу: &6" + entityName + " &fв виде: &a" + new DecimalFormat("#0.0").format((finalDamage / 10)) + " &c❤ / &c" + new DecimalFormat("#0.0").format((fullHp / 10)) + " &c❤")));
                    }

                }.runTaskTimer(hard, 0, 20);
            }
        }
    }

    @EventHandler
    public void onExit(PlayerQuitEvent e) {
        HandlerList.unregisterAll(this);

        p = null;
        hard = null;
        isSendDamage = false;
        finalDamage = 0d;
        entityName = null;
    }

}
