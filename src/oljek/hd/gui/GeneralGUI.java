package oljek.hd.gui;

import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.gui.custom.GUIPage;
import oljek.hd.object.Setting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class GeneralGUI extends GUI {

    public GeneralGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Главное меню";
    }

    @Override
    public int getSlot() {
        return 45;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();

        Setting setting = hard.getHardManager().getSetting(p.getName());

        items.add(new GUIItem(ItemUtil.create(Material.PAPER)
                .setName("&bБыстрые сообщения")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                        "",
                        " &7>> &fДоступно от: &3&lLORD &fи выше",
                        "",
                        "&7Дает вомзожность использовать готовые",
                        "&7символьные словосочетания в чате.")
                .getStack())
                .setSlot(20)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    if (!p.hasPermission(hard.getStorage().permissions.get("fastmessage")))
                        return;

                    GUIPage fastMessageGUI = new FastMessagePageGUI(hard, p);
                    fastMessageGUI.open();
                }));

        items.add(new GUIItem(ItemUtil.create(Material.NETHER_STAR)
                .setName("&bСмена цвета префикса")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                        "",
                        " &7>> &fДоступно от: &3&lLORD &fи выше",
                        "",
                        "&7Дает вомзожность сменить цвет",
                        "&7префикса как в табе, так и в чате.")
                .getStack())
                .setSlot(29)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    if (!p.hasPermission(hard.getStorage().permissions.get("customprefix")))
                        return;

                    //TODO
                }));

        items.add(new GUIItem(ItemUtil.create(Material.COMPASS)
                .setName("&bСообщение при входе")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                        "",
                        " &7>> &fДоступно от: &6&lPrince &fи выше",
                        "",
                        "&7Дает вомзожность выбрать самому",
                        "&7вариант цветного сообщения в чате",
                        "&7при входе на сервер.")
                .getStack())
                .setSlot(11)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    if (!p.hasPermission(hard.getStorage().permissions.get("messagejoin")))
                        return;

                    //TODO
                }));

        items.add(new GUIItem(ItemUtil.create(Material.REDSTONE)
                .setName("&bНастройки")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                        "",
                        "&eНа данный момент настройки такие:",
                        " &7··• &fКровь: " + getMessage(setting.isEnableBlood()),
                        " &7··• &fСообщение при входе: " + getMessage(setting.enableJoinMessage()),
                        " &7··• &fСкорборд: " + getMessage(setting.isShowScoreboard()),
                        " &7··• &fЧастицы при выделении территории: " + getMessage(setting.isSendParticleWand()) + " ")
                .getStack())
                .setSlot(23)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUI settingGUI = new SettingGUI(hard, p);
                    settingGUI.open();
                }));

        items.add(new GUIItem(ItemUtil.create(Material.BOOK)
                .setName("&bИнформация")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для просмотра",
                        "",
                        "&7Основная информация о сервере &c&lHARD.")
                .getStack())
                .setSlot(24)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    GUI infoGUI = new InfoGUI(hard, p);
                    infoGUI.open();
                }));

        items.add(new GUIItem(ItemUtil.create(Material.ENCHANTED_BOOK)
                .setName("&bПривилегии")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для просмотра",
                        "",
                        "&7Все привилеги на сервере.")
                .getStack())
                .setSlot(25)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                }));

        items.add(new GUIItem(ItemUtil.create(Material.WATCH)
                .setName("&bСмена анимации префикса в табе")
                .addLore("&7>> Нажмите ЛКМ/ПКМ для просмотра",
                        "",
                        " &7>> &fДоступно от &d&lEMPEROR &fи выше!",
                        "",
                        "&7Вы сможете сменить анимацию префикса",
                        "&7в табе. Попробуй ;)")
                .getStack())
                .setSlot(22)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    ChangeAnimatePageGUI changeAnimatePageGUI = new ChangeAnimatePageGUI(hard, p);
                    changeAnimatePageGUI.open();
                }));
        return items;
    }

    private String getMessage(boolean bool) {
        return (bool ? "&aвключено" : "выключено");
    }

}
