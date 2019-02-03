package oljek.hd.util;

import com.oljek.main.util.StringUtil;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import me.TechsCode.UltraPermissions.storage.objects.User;

public class RankUtil {

    public static String getGroup(String name) {
        User user = UltraPermissions.getAPI().getUsers().name(name);

        if (user == null)
            return "Player";

        if (user.getGroups() == null)
            return "Player";

        if (user.getGroups().get().length < 1)
            return "Player";

        return user.getGroups().get()[0].getName();
    }

    public static String getPrefix(String name) {
        User user = UltraPermissions.getAPI().getUsers().name(name);

        return getPrefix(user);
    }

    public static String getPrefix(User user) {
        if (user == null)
            return "§cNONE";

        if (user.getPrefix() == null || user.getPrefix().isEmpty()) {
            if (user.getGroups() == null)
                return "§cNONE";

            if (user.getGroups().get().length < 1)
                return "§cNONE";

            if (user.getGroups().get()[0].getPrefix() == null || user.getGroups().get()[0].getPrefix().isEmpty())
                return "§cNONE";

            return StringUtil.inColor(user.getGroups().get()[0].getPrefix());
        }

        return StringUtil.inColor(user.getPrefix());
    }

    public static String getPrefix(Group group) {
        if (group == null)
            return "§cNONE";

        if (group.getPrefix() == null || group.getPrefix().isEmpty())
            return "§cNONE";

        return StringUtil.inColor(group.getPrefix());
    }

}
