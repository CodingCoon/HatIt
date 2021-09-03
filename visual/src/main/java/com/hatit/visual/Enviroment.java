package com.hatit.visual;

import com.hatit.data.tournament.Tournament;
import com.hatit.io.IOService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Enviroment {
    //_______________________________________________ Parameters
    private final ObservableList<Tournament> allTournaments = FXCollections.observableArrayList();
    private final ObjectProperty<Tournament> currentTournament = new SimpleObjectProperty<>();

    private final BooleanProperty hasChanges = new SimpleBooleanProperty(false);

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
        List<Tournament> tournaments = IOService.createService().loadTournaments();
        allTournaments.addAll(tournaments);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
