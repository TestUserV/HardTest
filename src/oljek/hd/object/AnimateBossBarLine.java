package oljek.hd.object;

import com.oljek.main.util.StringUtil;
import org.bukkit.boss.BarColor;

public class AnimateBossBarLine {

    private String message;
    private BarColor color;
    private double valueTime;
    private double updateTime;

    public AnimateBossBarLine(String message, BarColor color, double valueTime, double updateTime) {
        this.message = message;
        this.color = color;
        this.valueTime = valueTime;
        this.updateTime = updateTime;
    }

    public static AnimateBossBarLine fromLine(String line) {
        String[] globalSplit = line.split("/%/");

        String message = null;
        BarColor color = null;
        double valueTime = 0d;
        double updateTime = 0d;

        for (String splitLine : globalSplit) {
            String[] internalSeparation = splitLine.split("=");

            if (internalSeparation.length < 2)
                continue;

            String path = internalSeparation[0];
            String value = internalSeparation[1];

            switch (path.toLowerCase()) {
                case "message": {
                    message = StringUtil.inColor(value);
                    break;
                }

                case "color": {
                    color = BarColor.valueOf(value.toUpperCase());
                    break;
                }

                case "value": {
                    valueTime = Double.valueOf(value);
                    break;
                }

                case "update": {
                    updateTime = Double.valueOf(value);
                    break;
                }
            }
        }

        if (message == null || color == null || valueTime == 0d || updateTime == 0d)
            return new AnimateBossBarLine("error", BarColor.RED, 1, 0);

        return new AnimateBossBarLine(message, color, valueTime, updateTime);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BarColor getColor() {
        return color;
    }

    public void setColor(BarColor color) {
        this.color = color;
    }

    public double getValueTime() {
        return valueTime;
    }

    public void setValueTime(double valueTime) {
        this.valueTime = valueTime;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(double updateTime) {
        this.updateTime = updateTime;
    }

}
