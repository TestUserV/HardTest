package oljek.hd.object;

public enum Color {

    WHITE("f"), DARK_WHITE("7"), GRAY("8"), BLACK("0"), DARK_RED("4"), RED("c"), YELLOW("e"), DARK_YELLOW("6"), DARK_GREEN("2"), GREEN("a"),
    AQUA("b"), DARK_AQUA("3"), DARK_BLUE("1"), BLUE("9"), PINK("d"), DARK_PINK("5");

    private String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return "&" + color;
    }

}
