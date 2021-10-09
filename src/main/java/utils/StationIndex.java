package utils;

import core.Line;
import core.Station;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StationIndex {
    private static final int NUMBER_OF_STATIONS = 330;

    private final List<Line> lines;
    private final transient List<Station> stationsList;
    private Map<String, List<String>> stations;
    private final Map<Station, List<Station>> connections;

    public StationIndex() {
        lines = new ArrayList<>();
        stationsList = new ArrayList<>();
        stations = new LinkedHashMap<>();
        connections = new LinkedHashMap<>();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }

    public Line getLine(String number) {
        return lines.stream()
                .filter(l -> l.getNumber().equals(number))
                .findAny().orElse(null);
    }

    public void addStation(Station station) {
        stationsList.add(station);
        if (stationsList.size() == NUMBER_OF_STATIONS) {
            setStationsAsMap();
        }
    }

    public Station getStation(String lineNumber, String name) {
        for (Station station : stationsList) {
            if (station.getName().equals(name) && station.getLine().getNumber().equals(lineNumber)) {
                return station;
            }
        }
        return null;
    }

    public void setStationsAsMap() {
        stations = stationsList.stream()
                .collect(Collectors.groupingBy(s -> s.getLine().getNumber(),
                        LinkedHashMap::new,
                        Collectors.mapping(Station::getName, Collectors.toList())));
    }

    public Map<String, List<String>> getStations() {
        return stations;
    }

    public boolean isStationCreated(String lineNumber, String name) {
        Station station = new Station(getLine(lineNumber), name);
        return stationsList.contains(station);
    }

    private boolean isConnected(List<Station> stations) {
        for (Station station : stations) {
            if (connections.containsKey(station)) {
                return true;
            }
        }
        return false;
    }

    public void addConnection(List<Station> stations) {
        if (!isConnected(stations)) {
            Station connection = stations.get(0);
            stations.remove(0);
            connections.put(connection, stations);
        }
    }

    public Map<Station, List<Station>> getConnections() {
        return connections;
    }

    public void countStationsByLine() {
        System.out.println("Количество станций на каждой линии:");
        for (Map.Entry<String, List<String>> line : stations.entrySet()) {
            System.out.println("На линии " + line.getKey() + " - " + line.getValue().size() + " станций");
        }
    }

    public void countConnections() {
        System.out.println("В московском метро " + connections.keySet().size() + " переходов");
    }
}
