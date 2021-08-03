package com.hatit.data.tournament;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.generation.Preferences;
import com.hatit.data.player.Player;
import com.hatit.data.team.Team;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.UUID;

public interface Tournament {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Tournament of() {
        return new TournamentImpl();
    }

    //_______________________________________________ Methods
    UUID getId();

    StringProperty propName();

    ReadOnlyObjectProperty<TournamentState> getState();

    void nextState();

    void previousState();

    ObservableList<Criteria> propCriteria();

    ObservableList<Player> propPlayers();

    Preferences getPreferences();

    ReadOnlyListWrapper<Team> getTeams();

    void generateTeams();

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
