package gson.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import core.Line;
import core.Station;
import utils.StationIndex;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationIndexDeserialize implements JsonDeserializer<StationIndex> {
    @Override
    public StationIndex deserialize(JsonElement jsonElement, Type type,
                                    JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        StationIndex stationIndex = new StationIndex();

        JsonArray lines = jsonObject.getAsJsonArray("lines");
        for (JsonElement line : lines) {
            stationIndex.addLine(context.deserialize(line, Line.class));
        }

        JsonObject stations = jsonObject.getAsJsonObject("stations");
        for (Map.Entry<String, JsonElement> entry : stations.entrySet()) {
            JsonArray stationsList = entry.getValue().getAsJsonArray();
            for (JsonElement station : stationsList) {
                stationIndex.addStation(new Station(stationIndex.getLine(entry.getKey()),
                        station.getAsString()));
            }
        }

        JsonArray connectionsArray = jsonObject.getAsJsonArray("connections");
        for (JsonElement connections : connectionsArray) {
            JsonArray connection = connections.getAsJsonArray();
            List<Station> connectionsList = new ArrayList<>();
            for (JsonElement stationAtConnection : connection) {
                JsonObject object = stationAtConnection.getAsJsonObject();
                String lineNumber = object.get("line").getAsString();
                String nameOfStation = object.get("station").getAsString();
                if (!stationIndex.isStationCreated(lineNumber, nameOfStation)) {
                    connectionsList.add(new Station(stationIndex.getLine(lineNumber),
                            nameOfStation));
                } else {
                    connectionsList.add(stationIndex.getStation(lineNumber, nameOfStation));
                }
            }
            stationIndex.addConnection(connectionsList);
        }

        return stationIndex;
    }
}
