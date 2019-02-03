package oljek.hd.util;

import com.google.common.collect.Lists;
import oljek.hd.Hard;
import oljek.hd.api.manager.HardManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeUtil {

    public static List<String> getHomes(String owner, String name, HardManager hardManager) {
        String[] homes = hardManager.getAllForPlayer(owner, false);

        if (homes.length > 0) {
            return Arrays.stream(homes)
                    .filter((s) -> {
                        if (name != null && !name.isEmpty())
                            return s.contains(name);

                        return true;
                    }).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

    public static List<String> getTab(CommandSender sender, String[] args, Hard hard) {
        HardManager hardManager = hard.getHardManager();

        if (args.length == 0)
            return HomeUtil.getHomes(sender.getName(), "", hardManager);
        else if (args.length == 1)
            return HomeUtil.getHomes(sender.getName(), args[0], hardManager);

        return Lists.newArrayList();
    }

}
