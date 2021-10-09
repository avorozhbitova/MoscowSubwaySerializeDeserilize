package core;

import java.util.Objects;

public class Line implements Comparable<Line>{
    private final String number;
    private final String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Номер линии: " + number + "   " +
                "Название линии: " + name;
    }

    private static boolean isNumeric(String s) {
        if (s == null) {
            return false;
        }
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public int compareTo(Line lineToCompare) {
        if (isNumeric(number) && isNumeric(lineToCompare.getNumber())) {
            return Integer.compare(Integer.parseInt(number), Integer.parseInt(lineToCompare.getNumber()));
        }
        if (number.matches("\\d+\\w+") && isNumeric(lineToCompare.getNumber())) {
            int a = Integer.parseInt(number.substring(0, number.length() - 1));
            if (Integer.parseInt(lineToCompare.getNumber()) == a) {
                return 1;
            }
            return Integer.compare(a, Integer.parseInt(lineToCompare.getNumber()));
        }
        if (isNumeric(number) && lineToCompare.getNumber().matches("\\d+\\w+")) {
            int a = Integer.parseInt(lineToCompare.getNumber().substring(0, lineToCompare.getNumber().length() - 1));
            return Integer.compare(Integer.parseInt(number), a);
        }
        if (number.matches("\\w+\\d+")) {
            return number.compareTo(lineToCompare.getNumber());
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Line) obj) == 0;
    }
}
