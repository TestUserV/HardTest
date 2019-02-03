package oljek.hd.storage;

import com.oljek.spigot.config.Config;
import com.oljek.spigot.config.ConfigMap;
import com.oljek.spigot.config.Storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigStorage extends Storage {

    @ConfigMap(value = "home.max.amount", classKeyName = "String", classValueName = "String")
    public Map<String, Integer> maxAmountHomes;

    @Config(value = "help.message", defaultList = {
            "&c&lHARD &7>> &fДоступные Вам команды:",
            "&7>> /help &c- все доступные команды",
            "&7>> /h (home) &c- посмотреть список/телепортироваться в дом",
            "&7>> /sh (sethome) &e$name &c- поставить дом",
            "&7>> /dh (deletehome) &e$name &c- удалить дом",
            "&7>> /w (warp) &c- взаимодействие с варпами",
            "&7>> /spawn &c- телепортироваться на спавн",
            "&7>> /kit &c- взаимодействие с китами",
            "&7>> /list &c- посмотреть список всех игроков на сервере",
            "&7>> /afk &c- войти в режим бездействия",
            "&7>> /msg (m) &e$player $message... &c- написать сообщение игроку",
            "&7>> /call &c- телепортироваться к игроку"
    })
    public List<String> helpMessages;

    @Config(value = "gui.info.bank.lore", defaultList = {
            " &7Банки - одна из важнейших частей на сервере",
            " &c&lHARD. &7На них держиться вся экономика.",
            " &7У Вас есть три счета, основной (который Вы можете",
            " &7везде носить с собой, банковский (работает как сейф,",
            " &7/bank teleport, /bank), карточный (переносной счет).",
            " &7Отличия основного от карточного заключается в том, что",
            " &7на основном счету есть ограничение в балансе размером в ",
            " &7150.000 монет, а у карточки они расширяемые.",
            " &7Карточки могут использоваться в магазинах, приватах и т.д"
    })
    public List<String> guiInfoBankLore;

    @Config(value = "gui.info.bank.name", defaultValue = "&6Банки")
    public String guiInfoBankName;

    @Config(value = "scoreboard.display.animated", defaultList = {
            "&c&lHARD"
    })
    public List<String> sbDisplayAnimated;

    @Config(value = "scoreboard.display.speed", defaultDouble = 0.3)
    public double sbDisplaySpeed;

    @Config(value = "scoreboard.global.content.speed", defaultDouble = 60d)
    public double sbChangeGlobalSpeed;

    @Config(value = "scoreboard.bank.content.speed", defaultDouble = 60d)
    public double sbChangeBankSpeed;

    @Config(value = "scoreboard.pvp.content.speed", defaultDouble = 60d)
    public double sbChangePVPSpeed;

    @ConfigMap(value = "permissions", classKeyName = "String", classValueName = "String")
    public Map<String, String> permissions;

    @ConfigMap(value = "group.value", classValueName = "String", classKeyName = "String")
    public Map<String, Integer> groupValue;

    @Config(value = "scoreboard.global.lore.content", defaultList = {
            "&7$time",
            "&f",
            " &fРанг:",
            "  &7>> &6$prefix",
            "&f&f",
            " &fБаланс:",
            "  &7>> &fОсн. счет: &a$moneyGlobal",
            "  $ifcard&7>> &fСчет карты: &a$moneyCard",
            "  &7>> &fСчет банка: &a$moneyBank",
            "&f&f&f",
            " &fСтатистика:",
            "  &7>> &fУбийств: &c$kills",
            "  &7>> &fСмертей: &c$death",
            "  &7>> &fK/D (У/С): &c$kd",
            "  $ifleague&7>> &fЛига: &c$league"
    })
    public List<String> sbGlobalContent;

    @Config(value = "scoreboard.bank.lore.content", defaultList = {
            "&7$time",
            "&f",
            " &6Рейтинг богачей (по счету банка)",
            " $rating&7$number. &7>> &c$name: &f$statistic",
            "&f&f",
            "&bhttp://www.proempire.ru/"
    })
    public List<String> sbBankContent;

    @Config(value = "scoreboard.pvp.lore.content", defaultList = {
            "&7$time",
            "&f",
            " &6Рейтинг убийств",
            " $rating&7$number. >> &c$name: &f$statisticKill / $statisticDeath",
            "&f&f",
            "&bhttp://www.proempire.ru/"
    })
    public List<String> sbPVPContent;

    @Config(value = "bossbar.animate", defaultList = {
            "/%/message=&fHello &a$player!/%/color=red/%/value=1.0/%/update=5.0"
    })
    public List<String> bossBarAnimate;

    @Config(value = "message.join.groups", defaultList = {
            "Prince",
            "King",
            "Caesar",
            "Emperor",
            "Helper",
            "Moder",
            "SrModer",
            "Admin",
    })
    public List<String> groupsMessageJoin;

    @Config(value = "message.fast.list", defaultList = {
            "&fTest &c:)"
    })
    public List<String> fastMessage;

    @Config(value = "message.motd.content", defaultList = {
            "",
            "                   &fПриветствуем &6$name &fна сервере &c&lHARD",
            "       &6&lOlJeK, molchic, KeFiR4iG &fжелают Вам удачного времяпрепровождения!",
            ""
    })
    public List<String> motd;

    @ConfigMap(value = "donate.change.color.prefix", classKeyName = "String", classValueName = "String")
    public Map<String, String> donateColors;

    public ConfigStorage() {
        maxAmountHomes = new HashMap<>();
        permissions = new HashMap<>();
        groupValue = new HashMap<>();
        donateColors = new HashMap<>();

        maxAmountHomes.put("Default", 2);
        maxAmountHomes.put("Lord", 3);
        maxAmountHomes.put("Prince", 7);
        maxAmountHomes.put("King", 8);
        maxAmountHomes.put("Caesar", 9);
        maxAmountHomes.put("Emperor", 12);

        permissions.put("gamemode", "Hard.command.gamemode");
        permissions.put("warp", "Hard.command.warp");
        permissions.put("warp.delete.other", "Hard.command.warp.delete.other");
        permissions.put("setspawn", "Hard.command.setspawn");
        permissions.put("kit_managment", "Hard.command.kit.managment");
        permissions.put("kit_delete", "Hard.kit.delete");
        permissions.put("kit_give", "Hard.kit.give");
        permissions.put("suicide", "Hard.command.suicide");
        permissions.put("feed", "Hard.command.feed");
        permissions.put("feed_other", "Hard.command.feed.other");
        permissions.put("workbench", "Hard.command.workbench");
        permissions.put("cinventory", "Hard.command.clearinventory");
        permissions.put("top", "Hard.command.top");
        permissions.put("back", "Hard.command.back");
        permissions.put("enderchest", "Hard.command.enderchest");
        permissions.put("heal", "Hard.command.heal");
        permissions.put("heal_other", "Hard.command.heal.other");
        permissions.put("firework", "Hard.command.firework");
        permissions.put("fly", "Hard.command.fly");
        permissions.put("fly_other", "Hard.command.fly.other");
        permissions.put("ptime", "Hard.command.ptime");
        permissions.put("repair", "Hard.command.repair");
        permissions.put("ext", "Hard.command.ext");
        permissions.put("ext_other", "Hard.command.ext.other");
        permissions.put("god", "Hard.command.god");
        permissions.put("god_other", "Hard.command.god.other");
        permissions.put("near", "Hard.command.near");
        permissions.put("tp", "Hard.command.tp");
        permissions.put("break", "Hard.command.break");
        permissions.put("speed", "Hard.command.speed");
        permissions.put("speed_custom", "Hard.command.speed.custom");
        permissions.put("seen", "Hard.command.seen");
        permissions.put("antioch", "Hard.command.antioch");
        permissions.put("spawner", "Hard.command.spawner");
        permissions.put("broadcast", "Hard.command.broadcast");
        permissions.put("tphere", "Hard.command.tphere");
        permissions.put("fireball", "Hard.command.fireball");
        permissions.put("invsee", "Hard.command.invsee");
        permissions.put("burn", "Hard.command.burn");
        permissions.put("fastmessage", "Hard.gui.fastmessage");
        permissions.put("customprefix", "Hard.gui.customprefix");
        permissions.put("messagejoin", "Hard.gui.messagejoin");
        permissions.put("hard", "Hard.command.hard");
        permissions.put("fastmessage", "Hard.command.fastmessage");

        groupValue.put("Default", 1);
        groupValue.put("Lord", 2);
        groupValue.put("Prince", 3);
        groupValue.put("King", 4);
        groupValue.put("Caesar", 5);
        groupValue.put("Emperor", 6);

        donateColors.put("Lord", "DARK_BLUE");
        donateColors.put("Prince", "DARK_PINK,DARKBLUE");
        donateColors.put("King", "DARK_PINK,DARKBLUE,");
    }

    @Override
    public String getDefaultConfigName() {
        return "config";
    }

}
