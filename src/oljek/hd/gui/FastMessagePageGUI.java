package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import oljek.cl.ClanPlugin;
import oljek.cl.api.Clan;
import oljek.cl.util.ClanUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.util.PageUtil;
import oljek.hd.util.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FastMessagePageGUI extends GUIPage {

    public FastMessagePageGUI(Hard hard, Player p) {
        super(hard, p, false);
    }

    @Override
    public List<GUIItem> getItemsAdditionally() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "&0Быстрые сообщения";
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new LinkedList<>();
        List<String> messages = hard.getStorage().fastMessage;

        for (String s : messages) {
            s = StringUtil.inColor(s);

            String finalS = s;
            items.add(new GUIItem(ItemUtil.create(Material.BOOK)
                    .setName(s)
                    .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                            "",
                            "&7Отправляет сообщение в чат")
                    .getStack())
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);

                        p.closeInventory();

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                Clan clan = ClanPlugin.getInstance().getClanManager().getClanByPlayer(p.getName());

                                TextComponent[] components = null;
                                TextComponent clanComponent = null;
                                TextComponent prefixComponent = null;
                                TextComponent chatComponent = null;

                                String prefix = RankUtil.getPrefix(p.getName());

                                if (!clan.getOwner().isEmpty())
                                    clanComponent = ClanUtil.getComponentClan(clan, p.getName());

                                chatComponent = new TextComponent(StringUtil.inColor(finalS));
                                prefixComponent = new TextComponent(StringUtil.inColor(prefix + " " + p.getName() + ": &f"));

                                chatComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(StringUtil.inColor("&cОтправлено с помощью &lбыстрого сообщения"))));
                                chatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fm"));

                                if (clanComponent == null)
                                    components = new TextComponent[]{prefixComponent, chatComponent};
                                else
                                    components = new TextComponent[]{clanComponent, prefixComponent, chatComponent};

                                for (Player p : Bukkit.getOnlinePlayers())
                                    p.spigot().sendMessage(components);
                            }

                        }.runTaskAsynchronously(hard);
                    }));
        }
        return PageUtil.toPage(items);
    }

}
