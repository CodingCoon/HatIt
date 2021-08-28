package com.hatit.data.tournament;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.generation.Preferences;
import com.hatit.data.player.Player;
import com.hatit.data.team.Team;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.UUID;

class TournamentImpl implements Tournament {

    //_______________________________________________ Parameters
    private static int COUNTER = 1;

    private final UUID id;
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<TournamentState> state = new SimpleObjectProperty<>(TournamentState.TOURNAMENT);

    private final ObservableList<Criteria>    criteria    = FXCollections.observableArrayList();
    private final ObservableList<Player>      players     = FXCollections.observableArrayList();
    private final Preferences preferences;

    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ReadOnlyListWrapper<Team> readOnlyTeams = new ReadOnlyListWrapper<>(teams);

    //_______________________________________________ Initialize
    TournamentImpl() {
        this(UUID.randomUUID(), "Turnier #" + COUNTER++, TournamentState.TOURNAMENT);
    }

    public TournamentImpl(UUID id, String name, TournamentState state) {
        this.id = id;
        this.name.setValue(name);
        this.state.set(state);
        this.preferences = Preferences.of(this);
    }

    //_______________________________________________ Methods
    @Override
    public String toString() {
        return name.getValueSafe().isBlank() ? id.toString() : name.get();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public StringProperty propName() {
        return name;
    }

    @Override
    public ReadOnlyObjectProperty<TournamentState> getState() {
        return state;
    }

    @Override
    public void nextState() {
        int ordinal = state.get().ordinal();
        assert ordinal < TournamentState.values().length - 1;
        state.setValue(TournamentState.values()[ordinal + 1]);
    }

    @Override
    public void previousState() {
        int ordinal = state.get().ordinal();
        assert ordinal > 0;
        state.setValue(TournamentState.values()[ordinal - 1]);
    }

    @Override
    public ObservableList<Criteria> propCriteria() {
        return criteria;
    }

    @Override
    public ObservableList<Player> propPlayers() {
        return players;
    }

    @Override
    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public ReadOnlyListWrapper<Team> getTeams() {
        return readOnlyTeams;
    }

    @Override
    public void generateTeams() {
        this.teams.clear();
        List<Team> start = new Generation(this).start();
        this.teams.addAll(start);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
