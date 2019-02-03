package oljek.hd.object;

import me.TechsCode.UltraPermissions.storage.objects.User;
import oljek.hd.Hard;
import oljek.hd.gui.custom.GUIItem;

public class PlayerComparable implements Comparable {

    private User user;
    private Hard hard;

    public PlayerComparable(User user, Hard hard) {
        this.user = user;
        this.hard = hard;
    }

    @Override
    public int compareTo(Object o) {
        GUIItem guiItem = (GUIItem) o;
        PlayerComparable playerComparable = (PlayerComparable) guiItem.getDate().getVar1();

        if (user.getGroups() == null || user.getGroups().get().length < 1) {
            if (playerComparable.user.getGroups() == null || playerComparable.user.getGroups().get().length < 1)
                return 0;
            else
                return 1;
        } else if (playerComparable.user.getGroups() == null || playerComparable.user.getGroups().get().length < 1)
            return -1;

        String group = user.getGroups().get()[0].getName();
        String groupNext = playerComparable.user.getGroups().get()[0].getName();

        if (!hard.getStorage().groupValue.containsKey(group) || !hard.getStorage().groupValue.containsKey(groupNext))
            return 0;

        if (hard.getStorage().groupValue.get(group) > hard.getStorage().groupValue.get(groupNext))
            return -1;
        else if (hard.getStorage().groupValue.get(group) < hard.getStorage().groupValue.get(groupNext))
            return 1;
        return 0;
    }
}
