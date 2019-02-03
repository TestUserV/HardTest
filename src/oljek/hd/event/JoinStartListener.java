package oljek.hd.event;

import oljek.hd.Hard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinStartListener implements Listener {

    private Hard hard;

    public JoinStartListener(Hard hard) {
        this.hard = hard;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (!hard.isEnable()) {
            e.setKickMessage("§bСервер ещё загружается!");
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

}
