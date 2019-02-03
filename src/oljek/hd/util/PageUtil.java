package oljek.hd.util;

import oljek.hd.gui.custom.GUIItem;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    public static List<GUIItem> toPage(List<GUIItem> items) {
        List<GUIItem> finalItems = new ArrayList<>();

        int cursor = 0;
        int page = 1;

        for (GUIItem item : items) {
            if (cursor > 21) {
                cursor = 0;
                page++;
            }

            item.setPage(page);
            finalItems.add(item);
        }

        return finalItems;
    }

}
