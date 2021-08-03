package com.hatit.visual.beige.teams;

import com.hatit.data.team.Team;
import com.hatit.data.tournament.Tournament;
import javafx.scene.layout.TilePane;

import java.util.List;

public class TeamOverview extends TilePane {
    //_______________________________________________ Parameters
    private final List<Team> teams;

    //_______________________________________________ Initialize
    public TeamOverview(Tournament tournament) {
        this.teams = tournament.getTeams();


        setHgap(12);
        setVgap(12);
        initUI();
    }

    //_______________________________________________ Methods
    private void initUI() {
        for (Team team : teams) {
            TeamView teamView = new TeamView(team);
            getChildren().add(teamView);
        }

    }
    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
