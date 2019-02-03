package oljek.hd.storage;

import com.google.common.collect.Lists;
import com.oljek.spigot.manager.ConfigManager;
import oljek.hd.Hard;
import oljek.hd.date.AnimateDate;
import oljek.hd.object.ArrayAnimateLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimateStorage {

    private Hard hard;
    private ConfigManager cfg;
    private Map<String, AnimateDate> animatedPrefix;

    public AnimateStorage(Hard hard) {
        this.hard = hard;

        cfg = new ConfigManager(hard, "animate");
        cfg.create();

        load();
    }

    private void load() {
        animatedPrefix = new HashMap<>();

        if (!cfg.getConfiguration().contains("tab.prefix")) {
            Map<String, List<String>> adminAnimated = new HashMap<>();
            Map<String, List<String>> coderAnimated = new HashMap<>();

            adminAnimated.put("animation1", Lists.newArrayList(
                    "&6&lC&7&lODER&6",
                    "&6&lAD&7&lMIN&6",
                    "&6&lADM&7&lIN&6",
                    "&6&lADMI&7&lN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&7&lADMI&6&lN&6",
                    "&7&lADM&6&lIN&6",
                    "&7&lAD&6&lMIN&6",
                    "&7&lA&6&lDMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6",
                    "&6&lADMIN&6"
            ));

            coderAnimated.put("animation2", Lists.newArrayList(
                    "&6&lC&7&lODER&6",
                    "&6&lCO&7&lDER&6",
                    "&6&lCOD&7&lER&6",
                    "&6&lCODE&7&lR&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&7&lCODE&6&lR&6",
                    "&7&lCOD&6&lER&6",
                    "&7&lCO&6&lDER&6",
                    "&7&lC&6&lODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6",
                    "&6&lCODER&6"
            ));

            cfg.getConfiguration().set("tab.prefix.admin.lines", Lists.newArrayList(adminAnimated));
            cfg.getConfiguration().set("tab.prefix.coder.lines", Lists.newArrayList(coderAnimated));
            cfg.getConfiguration().set("tab.prefix.coder.change-interval", 0.3d);
            cfg.getConfiguration().set("tab.prefix.admin.change-interval", 0.3d);
            cfg.save();
        }

        for (String group : cfg.getConfiguration().getConfigurationSection("tab.prefix").getKeys(false)) {
            if (cfg.getConfiguration().contains("tab.prefix." + group + ".lines") &&
                    cfg.getConfiguration().contains("tab.prefix." + group + ".change-interval")) {
                List<ArrayAnimateLine> listAnimateLines = new ArrayList<>();

                for (Map<?, ?> map : cfg.getConfiguration().getMapList("tab.prefix." + group + ".lines")) {
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        String name = (String) entry.getKey();
                        List<String> lines = (List<String>) entry.getValue();

                        listAnimateLines.add(new ArrayAnimateLine(lines, name));
                    }
                }

                AnimateDate date = new AnimateDate(listAnimateLines,
                        cfg.getConfiguration().getDouble("tab.prefix." + group + ".change-interval"));

                animatedPrefix.put(group, date);
            }
        }
    }

    public Map<String, AnimateDate> getAnimatedPrefix() {
        return animatedPrefix;
    }

}
