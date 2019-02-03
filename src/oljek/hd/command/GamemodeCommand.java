package oljek.hd.command;

import com.google.common.collect.Lists;
import com.oljek.main.util.StringUtil;
import com.oljek.spigot.command.custom.Command;
import com.oljek.spigot.command.custom.ConsoleExecute;
import com.oljek.spigot.command.custom.PlayerExecute;
import com.oljek.spigot.command.custom.TabExecute;
import oljek.hd.Hard;
import oljek.hd.date.DateMessage;
import oljek.hd.object.enums.GamemodeType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command({"gamemode", "gm"})
public class GamemodeCommand {

    private Hard hard;

    public GamemodeCommand(Hard hard) {
        this.hard = hard;
    }

    @PlayerExecute
    public void onPlayerExecute(Player p, String[] args) {
        if (!p.hasPermission(hard.getStorage().permissions.get("gamemode"))) {
            DateMessage.NO_PERMISSION.sendMessage(p);
            return;
        }

        if (args.length == 0) {
            if (p.getGameMode() != GameMode.SURVIVAL) {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы поставили себе режим: &cвыживания"));
                return;
            }

            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы поставили себе режим: &cкреатива"));
            return;
        }

        String type = args[0];
        GamemodeType gamemodeType = GamemodeType.getByType(type);

        if (gamemodeType != null) {
            checkGamemode(p, null, gamemodeType);
            return;
        } else {
            String target = args[0];
            Player playerTarget = Bukkit.getPlayer(target);

            if (playerTarget == null) {
                DateMessage.PLAYER_NOT_FIND.sendMessage(p, args[0]);
                return;
            }

            if (args.length == 1) {
                checkGamemode(playerTarget, p, null);
                return;
            }

            type = args[1];
            gamemodeType = GamemodeType.getByType(type);

            if (gamemodeType == null) {
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fТип: &c" + args[1] + " &fне найден!"));
                return;
            }

            checkGamemode(playerTarget, p, gamemodeType);
        }
    }

    private void checkGamemode(Player p, Player target, GamemodeType type) {
        String message = null;

        if (type == null) {
            if (p.getGameMode() != GameMode.SURVIVAL) {
                message = "&aвыживания";

                p.setGameMode(GameMode.SURVIVAL);
            } else {
                message = "&cкреатив";

                p.setGameMode(GameMode.CREATIVE);
            }
        } else {
            switch (type) {
                case SURVIVAL: {
                    message = "&aвыживания";
                    break;
                }

                case CREATIVE: {
                    message = "&cкреатива";
                    break;
                }

                case ADVENTURE: {
                    message = "&6приключения";
                    break;
                }

                case SPECTATOR: {
                    message = "&9наблюдения";
                    break;
                }
            }

            p.setGameMode(GameMode.valueOf(type.name().toUpperCase()));
        }

        if (target == null)
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы поставили себе режим: " + message));
        else {
            p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгрок: &c" + target.getName() + " &fпоставил Вам режим: " + message));
            target.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fИгроку: &c" + p.getName() + " &fбыл установлен режим: " + message));
        }
    }

    @ConsoleExecute
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /gm $player $type");
            return;
        }


    }

    @TabExecute
    public List<String> onTabExecute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Arrays.stream(GamemodeType.values())
                    .map((s) -> s.getTypeString())
                    .collect(Collectors.toList());
        }

        if (args.length == 1) {
            String type = args[0];
            List<String> tabs = getListByType(type);

            if (tabs.isEmpty())
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(HumanEntity::getName)
                        .filter((s) -> s.toLowerCase().contains(type.toLowerCase()))
                        .collect(Collectors.toList());

            return tabs;
        }

        if (args.length == 2) {
            String type = args[1];

            return getListByType(type);
        }

        return Lists.newArrayList();
    }

    private List<String> getListByType(String type) {
        try {
            int i = Integer.parseInt(type);

            if (Arrays.stream(GamemodeType.values())
                    .anyMatch((s) -> s.getTypeInteger() == i)) {
                return Arrays.stream(GamemodeType.values())
                        .filter((s) -> s.getTypeInteger() == i)
                        .map((s) -> String.valueOf(s.getTypeInteger()))
                        .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            if (Arrays.stream(GamemodeType.values()).anyMatch((s) -> s.getTypeString().contains(type))) {
                return Arrays.stream(GamemodeType.values())
                        .filter((s) -> s.getTypeString().contains(type))
                        .map(GamemodeType::getTypeString)
                        .collect(Collectors.toList());
            }
        }

        return Lists.newArrayList();
    }

}
