package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.deserialize.StationIndexDeserialize;
import gson.serialize.StationIndexSerialize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonHandler {

    private StationIndex stationIndex;

    public JsonHandler() {
    }

    public JsonHandler(StationIndex stationIndex) {
        this.stationIndex = stationIndex;
    }

    public void serializeStationIndex(String path) {
        System.out.println("Записываем список станций и линий в файл map.json..");
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(StationIndex.class, new StationIndexSerialize())
                .create();
        try {
            Files.write(Paths.get(path), gson.toJson(stationIndex).getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public StationIndex deserializeStationIndex(String path) {
        System.out.println("Читаем map.json..");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StationIndex.class, new StationIndexDeserialize())
                .create();
        String jsonToString = "";
        try {
            jsonToString = Files.readString(Paths.get(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return gson.fromJson(jsonToString, StationIndex.class);
    }
}
