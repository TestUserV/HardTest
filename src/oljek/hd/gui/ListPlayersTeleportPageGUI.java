package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.*;

public class ListPlayersTeleportPageGUI extends GUIPage {

    private static Map<UUID, Long> cooldownPlayers;
    private static List<Player> playersWait;

    static {
        playersWait = new ArrayList<>();
        cooldownPlayers = new HashMap<>();
    }

    private String search;

    public ListPlayersTeleportPageGUI(Hard hard, Player p, String search) {
        super(hard, p, false);

        this.search = search;
    }

    @Override
    public void open() {
        super.open();

        playersWait.add(p);
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        List<GUIItem> items = new ArrayList<>(1);

        items.add(new GUIItem(ItemUtil.create(Material.COMPASS)
                .setName("&9Поиск")
                .getStack())
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                }));

        return items;
    }

    @Override
    public String getName() {
        return "&0Список всех ожидающих игроков";
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        if (playersWait.size() == 1 && playersWait.get(0).equals(this.p)) {
            GUIItem item = new GUIItem(ItemUtil.create(Material.BARRIER)
                    .setName("&cОтсутствует.")
                    .getStack())
                    .setPage(1)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    });

            items.add(item);
        } else {
            int cursor = 0;
            int page = 1;

            for (Player player : playersWait) {
                if (cursor > 21) {
                    cursor = 0;
                    page++;
                }

                if (player.equals(this.p))
                    continue;

                GUIItem item = new GUIItem(ItemUtil.createHead(player.getName())
                        .setName("&c" + player.getName())
                        .getStack())
                        .setPage(page)
                        .setFunction((e) -> {
                            e.setCancelled(true);
                            e.setResult(Event.Result.DENY);

                            if (cooldownPlayers.containsKey(p.getUniqueId())) {
                                if (cooldownPlayers.get(p.getUniqueId()) < System.currentTimeMillis())
                                    cooldownPlayers.remove(p.getUniqueId());
                                else {
                                    p.closeInventory();
                                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВам нужно подождать: &c" + ((cooldownPlayers.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000) + " &fсек."));
                                    return;
                                }
                            }

                            cooldownPlayers.put(p.getUniqueId(), System.currentTimeMillis() + 1000 * 60);

                            GUI takingGui = new TakingDecisionGUI(hard, player, p);
                            takingGui.open();
                        });

                items.add(item);
            }
        }

        return items;
    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);

        if (playersWait.contains(e.getPlayer()))
            playersWait.remove(e.getPlayer());
    }

}
