package oljek.hd.gui;

import oljek.hd.Hard;
import oljek.hd.gui.custom.GUI;
import oljek.hd.gui.custom.GUIItem;
import oljek.hd.object.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChangeColorPrefixGUI extends GUI {

    public ChangeColorPrefixGUI(Hard hard, Player p) {
        super(hard, p);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getSlot() {
        return 45;
    }

    @Override
    public List<GUIItem> getItems() {
        List<GUIItem> items = new ArrayList<>();


        return null;
    }

    private int transformToDurabillity(Color color) {
        switch (color) {
            case DARK_RED:
                return 14;

            case RED:
                return 0;
        }

        return 1;
    }

}
