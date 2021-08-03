package com.hatit.visual;

import com.hatit.data.tournament.Tournament;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Enviroment {
    //_______________________________________________ Parameters
    private final ObservableList<Tournament> allTournaments = FXCollections.observableArrayList();
    private final ObjectProperty<Tournament> currentTournament = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    public Enviroment() {
        loadTournaments();
    }

    //_______________________________________________ Methods
    public ObjectProperty<Tournament> propCurrentTournament() {
        return currentTournament;
    }

    public Tournament getCurrentTournament() {
        return currentTournament.get();
    }

    public ObservableList<Tournament> getAllTournaments() { // TODO: read only
        return allTournaments;
    }

    public void addTournament(Tournament tournament) {
        allTournaments.add(tournament);
        currentTournament.setValue(tournament); // TODO:  check if there were changes
    }

    private void loadTournaments() {
        // TODO:
    }

    private void save() {
        // TODO:
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
