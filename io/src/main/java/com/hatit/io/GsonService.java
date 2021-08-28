package com.hatit.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.hatit.data.tournament.Tournament;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsonService implements IOService {

    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public List<Tournament> loadTournaments() {
        String dataFolder = System.getenv("LOCALAPPDATA") + "/"  + FILE_RESOURCE;
        File dataFile = new File(dataFolder);
        ArrayList<Tournament> tournaments = new ArrayList<>();
        File[] tournamentFiles = dataFile.listFiles();

        if (tournamentFiles != null) {
            for (File file : tournamentFiles) {
                Tournament tournament = loadTournament(file.getAbsolutePath());
                tournaments.add(tournament);
            }
        }
        return tournaments;
    }

    private Tournament loadTournament(String fileName) {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Tournament.class, new TournamentDeserializer())
                .registerTypeAdapter(Tournament.class, new TournamentDeserializer())
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();

        try (FileReader fileReader = new FileReader(fileName)) {
            JsonReader reader = new JsonReader(fileReader);
            return gson.fromJson(reader, Tournament.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

        String fileName = FILE_RESOURCE + tournament.getName() + EXTENSION;
        try (FileWriter writer = new FileWriter(fileName)){
            gson.toJson(tournament, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}