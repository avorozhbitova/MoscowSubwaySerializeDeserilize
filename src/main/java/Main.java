import utils.StationIndex;
import utils.HtmlFileParser;
import utils.JsonHandler;

public class Main {
    private static final String URL = "https://www.moscowmap.ru/metro.html#lines";
    private static final String DATA_FILE = "src/main/resources/map.json";

    private static StationIndex stationIndex;

    public static void main(String[] args) {
        createStationIndex();
        serializeStationIndex(stationIndex);

        StationIndex deserializedStationIndex = deserializeStationIndex();
        deserializedStationIndex.countStationsByLine();
        deserializedStationIndex.countConnections();
    }

    private static void createStationIndex() {
        stationIndex = new StationIndex();
        HtmlFileParser parser = new HtmlFileParser(URL, stationIndex);
        parser.parseHtml();
    }

    private static void serializeStationIndex(StationIndex stationIndex) {
        JsonHandler handler = new JsonHandler(stationIndex);
        handler.serializeStationIndex(DATA_FILE);
    }

    private static StationIndex deserializeStationIndex() {
        JsonHandler handler = new JsonHandler();
        return handler.deserializeStationIndex(DATA_FILE);
    }
}
