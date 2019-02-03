package oljek.hd.object;

import java.util.List;

public class ArrayAnimateLine {

    private List<String> lines;
    private String name;

    public ArrayAnimateLine(List<String> lines, String name) {
        this.lines = lines;
        this.name = name;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getName() {
        return name;
    }

}
