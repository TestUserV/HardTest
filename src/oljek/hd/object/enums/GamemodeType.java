package oljek.hd.object.enums;

import java.util.Arrays;

public enum GamemodeType {

    CREATIVE("creative", 1), SURVIVAL("survival", 0), ADVENTURE("adventure", 2), SPECTATOR("spectator", 3);

    private String typeString;
    private int typeInteger;

    GamemodeType(String typeString, int typeInteger) {
        this.typeInteger = typeInteger;
        this.typeString = typeString;
    }

    public static GamemodeType getByType(String type) {
        try {
            int i = Integer.parseInt(type);
            return Arrays.stream(GamemodeType.values()).filter((s) -> s.getTypeInteger() == i).findFirst().orElse(null);
        } catch (NumberFormatException e) {
            return Arrays.stream(GamemodeType.values()).filter((s) -> s.getTypeString().equals(type.toLowerCase())).findFirst().orElse(null);
        }
    }

    public int getTypeInteger() {
        return typeInteger;
    }

    public String getTypeString() {
        return typeString;
    }

}
