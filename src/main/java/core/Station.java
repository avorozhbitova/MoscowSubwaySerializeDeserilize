package core;

public class Station implements Comparable<Station> {
    private final Line line;
    private final String name;

    public Station(Line line, String name) {
        this.line = line;
        this.name = name;
    }

    public Line getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Номер линии: " + line.getNumber() + "   " +
                "Название станции: " + name;
    }

    @Override
    public int compareTo(Station station) {
        {
            int lineComparison = line.compareTo(station.getLine());
            if (lineComparison != 0) {
                return lineComparison;
            }
            return name.compareToIgnoreCase(station.getName());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Station) obj) == 0;
    }
}
