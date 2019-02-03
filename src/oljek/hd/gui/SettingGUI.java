package oljek.hd.gui;

import com.oljek.spigot.util.ItemUtil;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.object.Setting;
import oljek.hd.util.RankUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class SettingGUI extends GUI {

    public SettingGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return "&0Настройки";
    }

    @Override
    public int getSlot() {
        return 27;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();
        String group = RankUtil.getGroup(p.getName()).toLowerCase();
        Setting setting = hard.getHardManager().getSetting(p.getName());

        int bloodItemSlot = -1;
        int wandItemSlot = 3;
        int joinMessageItemSlot = -1;
        int scoreboardItemSlot = 5;
        int slotBlackPurpeColor[] = null;
        int slotPurpeColor[] = null;


        switch (group) {
            case "coder":
            case "admin":
            case "stmoder":
            case "moder":
            case "helper":

            case "emperor": {
                bloodItemSlot = 10;
                joinMessageItemSlot = 14;
                scoreboardItemSlot = 12;
                wandItemSlot = 16;

                slotBlackPurpeColor = new int[]{
                        1,
                        3,
                        5,
                        7,
                        9,
                        11,
                        13,
                        15,
                        17,
                        19,
                        21,
                        23,
                        25
                };

                slotPurpeColor = new int[]{
                        0,
                        2,
                        4,
                        6,
                        8,
                        18,
                        20,
                        22,
                        24,
                        26
                };
                break;
            }

            case "player": {
                scoreboardItemSlot = 12;
                wandItemSlot = 14;

                slotBlackPurpeColor = new int[]{
                        3,
                        5,
                        9,
                        11,
                        13,
                        15,
                        17,
                        20,
                        21,
                        23
                };

                slotPurpeColor = new int[]{
                        0,
                        1,
                        2,
                        6,
                        7,
                        8,
                        10,
                        16,
                        18,
                        19,
                        20,
                        22,
                        24,
                        25,
                        26
                };
                break;
            }

            default: {
                scoreboardItemSlot = 11;
                joinMessageItemSlot = 13;
                wandItemSlot = 15;

                slotBlackPurpeColor = new int[]{
                        2,
                        4,
                        6,
                        10,
                        12,
                        14,
                        16,
                        20,
                        22,
                        24
                };

                slotPurpeColor = new int[]{
                        0,
                        1,
                        3,
                        5,
                        7,
                        8,
                        9,
                        17,
                        18,
                        19,
                        21,
                        23,
                        25,
                        26
                };
                break;
            }
        }

        if (bloodItemSlot != -1) {
            String disable = (!setting.isEnableBlood() ? "&c◯ &c&nВыключено&f" : "&7◯ &c&mВыключено&f");
            String enable = (setting.isEnableBlood() ? "&a◯ &a&nВключено&f" : "&7◯ &a&mВключено&f");

            items.add(new GUIItem(ItemUtil.create(Material.INK_SACK)
                    .setDurabillity(DyeColor.RED.getDyeData())
                    .setName("&cКровь")
                    .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                            "",
                            "&7При ударе любого существа, на локации",
                            "&7будет кровь. (Частица)",
                            "",
                            " " + disable,
                            " " + enable)
                    .getStack())
                    .setSlot(bloodItemSlot)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);

                        setting.setEnableBlood(!setting.isEnableBlood());
                        new SettingGUI(hard, p).open();
                    }));
        }

        if (joinMessageItemSlot != -1) {
            String disable = (!setting.enableJoinMessage() ? "&c◯ &c&nВыключено&f" : "&7◯ &c&mВыключено&f");
            String enable = (setting.enableJoinMessage() ? "&a◯ &a&nВключено&f" : "&7◯ &a&mВключено&f");

            items.add(new GUIItem(ItemUtil.create(Material.PAPER)
                    .setName("&bСообщение при входе")
                    .addLore("&7>> Нажмите ЛКМ/ПКМ для действий",
                            "",
                            "&7При входе всем игрокам на сервере",
                            "&7будет отображено сообщение.",
                            "",
                            " " + disable,
                            " " + enable)
                    .getStack())
                    .setSlot(joinMessageItemSlot)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);

                        setting.setEnableMessage(!setting.enableJoinMessage());
                        new SettingGUI(hard, p).open();
                    }));
        }

        String disableWand = (!setting.isSendParticleWand() ? "&c◯ &c&nВыключено&f" : "&7◯ &c&mВыключено&f");
        String enablewWand = (setting.isSendParticleWand() ? "&a◯ &a&nВключено&f" : "&7◯ &a&mВключено&f");

        String disableScoreboard = (!setting.isShowScoreboard() ? "&c◯ &c&nВыключено&f" : "&7◯ &c&mВыключено&f");
        String enablewScoreboard = (setting.isShowScoreboard() ? "&a◯ &a&nВключено&f" : "&7◯ &a&mВключено&f");

        items.add(new GUIItem(ItemUtil.create(Material.WOOD_AXE)
                .setName("&aЧастицы при выделении")
                .addLore("&7>> Нажмите ПКМ/ЛКМ для действий",
                        "",
                        "&7При выделении деревянным топором,",
                        "&7будут созданы частицы по траектории",
                        "&7выделения.",
                        "",
                        " " + disableWand,
                        " " + enablewWand)
                .getStack())
                .setSlot(wandItemSlot)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    setting.setSendParticleWand(!setting.isSendParticleWand());
                    new SettingGUI(hard, p).open();
                }));

        items.add(new GUIItem(ItemUtil.create(Material.SIGN)
                .setName("&3Скорборд")
                .addLore("&7>> Нажмите ПКМ/ЛКМ для действий",
                        "",
                        "&7Выключает/включает Scoreboard",
                        "",
                        " " + disableScoreboard,
                        " " + enablewScoreboard)
                .getStack())
                .setSlot(scoreboardItemSlot)
                .setFunction((e) -> {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);

                    setting.setShowScoreboard(!setting.isShowScoreboard());
                    new SettingGUI(hard, p).open();
                }));

        for (int slot : slotBlackPurpeColor) {
            items.add(new GUIItem(ItemUtil.create(Material.STAINED_GLASS_PANE)
                    .setDurabillity(10)
                    .setName(" ")
                    .getStack())
                    .setSlot(slot)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    }));
        }

        for (int slot : slotPurpeColor) {
            items.add(new GUIItem(ItemUtil.create(Material.STAINED_GLASS_PANE)
                    .setDurabillity(2)
                    .setName(" ")
                    .getStack())
                    .setSlot(slot)
                    .setFunction((e) -> {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    }));
        }

        return items;
    }

}
