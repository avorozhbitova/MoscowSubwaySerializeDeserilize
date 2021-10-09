package utils;

import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlFileParser {
    private final static int START_OF_CHILDREN_DESCR_CONNECTIONS = 2;
    private final String URL;
    private Document document;
    private final StationIndex stationIndex;

    public HtmlFileParser(String URL, StationIndex stationIndex) {
        this.URL = URL;
        this.stationIndex = stationIndex;
    }

    public void parseHtml() {
        System.out.println("Парсим HTML-код страницы московского метрополитена..");
        try {
            document = Jsoup.connect(URL).maxBodySize(0).get();
            getLinesFromHtml();
            getStationsFromHtml(stationIndex.getLines());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void getLinesFromHtml() {
        Elements elements = document.select("span[data-line]");
        for (Element element : elements) {
            stationIndex.addLine(new Line(element.attr("data-line"), element.text()));
        }
    }

    private void getStationsFromHtml(List<Line> lines) {
        int previousNumber = 0;
        int indexOfLine = 0;

        Elements elements = document.select("a[data-metrost]");
        for (Element element : elements) {
            int numberOfStation = Integer.parseInt(element.child(0).text()
                    .substring(0, element.child(0).text().length() - 1));
            String nameOfStation = element.child(1).text();
            if (numberOfStation < previousNumber) {
                indexOfLine++;
            }
            Station station = new Station(lines.get(indexOfLine), nameOfStation);
            stationIndex.addStation(station);
            if (element.childNodeSize() > START_OF_CHILDREN_DESCR_CONNECTIONS) {
                getConnections(element, station);
            }
            previousNumber = numberOfStation;
        }
    }

    public void getConnections(Element element, Station station) {
        List<Station> connectedStations = new ArrayList<>();
        connectedStations.add(station);
        for (int i = START_OF_CHILDREN_DESCR_CONNECTIONS; i < element.childNodeSize(); i++) {
            String lineNumber = element.child(i).attr("class")
                    .replaceAll("t-icon-metroln ln-", "");
            String connectionInfo = element.child(i).attr("title");
            String name = connectionInfo
                    .substring(connectionInfo.indexOf("«") + 1, connectionInfo.indexOf("»"));
            if (stationIndex.isStationCreated(lineNumber, name)) {
                connectedStations.add(stationIndex.getStation(lineNumber, name));
            } else {
                connectedStations.add(new Station(stationIndex.getLine(lineNumber), name));
            }
        }
        stationIndex.addConnection(connectedStations);
    }
}