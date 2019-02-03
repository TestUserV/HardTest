package oljek.hd.date;

import com.oljek.main.util.StringUtil;
import org.bukkit.entity.Player;

public enum DateMessage {
    PREFIX("&c&lHARD &7>> &f"),
    NO_PERMISSION(true, "&cВы не имеете прав!"),
    PLAYER_NOT_FIND(true, "Игрок: &c$%0 &fоффлайн!");

    private String message;
    private String[] arguments;
    private boolean addPrefix;

    DateMessage(String message, String... arguments) {
        this(false, message, arguments);
    }

    DateMessage(boolean addPrefix, String message, String... arguments) {
        this.addPrefix = addPrefix;
        this.message = message;
        this.arguments = arguments;
    }

    public String getMessage(Object... arguments) {
        if (arguments.length > 0) {
            for (int i = 0; i < arguments.length; i++)
                message = message.replace("$%" + i, arguments[i] + "");
        }

        return (addPrefix ? StringUtil.inColor(PREFIX.getMessage() + this.message) : StringUtil.inColor(this.message));
    }

    public void sendMessage(Player p, Object... arguments) {
        p.sendMessage(getMessage(arguments));
    }
}
