package gson.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import core.Station;
import utils.StationIndex;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class StationIndexSerialize implements JsonSerializer<StationIndex> {
    @Override
    public JsonElement serialize(StationIndex stationIndex, Type type,
                                 JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.add("stations", context.serialize(stationIndex.getStations()));
        result.add("connections", serializeConnections(stationIndex, type, context));
        result.add("lines", context.serialize(stationIndex.getLines()));

        return result;
    }

    public JsonElement serializeConnections(StationIndex stationIndex, Type type,
                                 JsonSerializationContext context) {
        JsonArray connections = new JsonArray();
        for (Map.Entry<Station, List<Station>> entry : stationIndex.getConnections().entrySet()) {
            JsonArray stationsByConnection = new JsonArray();

            JsonObject stationFrom = new JsonObject();
            stationFrom.addProperty("line", entry.getKey().getLine().getNumber());
            stationFrom.addProperty("station", entry.getKey().getName());
            stationsByConnection.add(stationFrom);

            for (Station station : entry.getValue()) {
                JsonObject stationTo = new JsonObject();
                stationTo.addProperty("line", station.getLine().getNumber());
                stationTo.addProperty("station", station.getName());
                stationsByConnection.add(stationTo);
            }
            connections.add(stationsByConnection);
        }
    return connections;
    }
}
