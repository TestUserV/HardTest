package oljek.hd.event;

import com.oljek.spigot.listener.custom.UpdateEvent;
import oljek.hd.Hard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpdateListener implements Listener {

    private Hard hard;

    public UpdateListener(Hard hard) {
        this.hard = hard;
    }

    @EventHandler
    public void onUpdateScoreboard(UpdateEvent e) {
        /**if (e.getType() == UpdateType.TICK) {
         for (Player p: Bukkit.getOnlinePlayers()) {
         Setting setting = hard.getHardManager().getSetting(p.getName());

         if (setting.isEnableDynamicPrefix()) {
         Color random = Color.values()[new Random().nextInt(Color.values().length)];

         User user = UltraPermissions.getAPI().getUsers().name(p.getName());

         if (user != null) {
         String prefix = (user.getPrefix() == null ? user.getGroups() == null ? "Default" :
         user.getGroups().get().length < 1 ? "Default" : user.getGroups().get()[0].getPrefix() :
         user.getPrefix());

         prefix = StringUtil.stripColor(prefix, '§');
         prefix = StringUtil.stripColor(prefix, '&');

         prefix = com.oljek.main.util.StringUtil.inColor(random.getColor() + "&l" + prefix + random.getColor());

         user.setPrefix(prefix);
         user.save();

         TeamUtil.setPrefix(p);
         }
         }
         }
         }**/
    }

}
