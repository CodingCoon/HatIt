package com.hatit.io;

import com.google.gson.*;
import com.hatit.data.criteria.Criteria;
import com.hatit.data.tournament.Tournament;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("unchecked")
public class GsonService implements IOService {        // TODO:    other JSON library

    //_______________________________________________ Parameters

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public List<String> loadTournamentNames() {
        return null;
    }

    @Override
    public Tournament loadTournament(String fileName) {
        return null;
    }

    @Override
    public void store(Tournament tournament) {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Tournament.class, new TournamentSerializer())
                .registerTypeAdapter(Tournament.class, new TournamentSerializer())
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();

        try (FileWriter writer = new FileWriter(FILE_RESOURCE + tournament.propName().get() + EXTENSION)){
            gson.toJson(tournament, writer);
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }

    private final class TournamentSerializer implements JsonSerializer<Tournament> {
        @Override
        public JsonElement serialize(Tournament src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject tournamentJson = new JsonObject();
            tournamentJson.addProperty("id", src.getId().toString());
            tournamentJson.addProperty("name", src.propName().get());
            tournamentJson.addProperty("state", src.getState().get().name());

            JsonArray criteriaArray = src.propCriteria().stream().map(this::createCriteriaJson).collect(JsonArray::new, JsonArray::add, (jsonElements, jsonElements2) -> {});
            tournamentJson.add("criteria", criteriaArray);

            return tournamentJson;
        }

        private JsonElement createCriteriaJson(Criteria criteria) {
            JsonObject criteriaJson = new JsonObject();

            criteriaJson.addProperty("id", criteria.getID().toString());
            criteriaJson.addProperty("name", criteria.propName().get());
            criteriaJson.addProperty("type", criteria.propType().get().name());

            return criteriaJson;
        }
    }

//    public static void store(Tournament tournament) {
//        JSONObject root = new JSONObject();
//
//        root.put("id", tournament.getId());
//        root.put("name", tournament.propName().get());
//
//        root.put("criteria", createCriteriaJSON(tournament));
//        root.put("player", createPlayerJSON(tournament.propPlayers()));
//        root.put("preferences", createPreferencesJSON(tournament));
//
//        try (FileWriter writer = new FileWriter(FILE_RESOURCE + tournament.propName().get() + ".json")) {
//            writer.write(root.toJSONString());
//            writer.flush();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private static Object createPreferencesJSON(Tournament tournament) {
//        return null;
//    }
//
//    private static Object createPlayerJSON(List<Player> players) {
//        JSONObject playerObject = new JSONObject();
//
//        for (Player player : players) {
//            playerObject
//        }
//        return null;
//    }
//
//    private static Object createCriteriaJSON(Tournament tournament) {
//        return null;
//    }
//
//    public static List<Tournament> loadTournaments() {
//        return null;
//    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
