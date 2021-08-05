package com.hatit.visual.beige.teams;

import com.hatit.data.team.Team;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import javafx.scene.layout.TilePane;

import java.util.List;

public class TeamOverview extends TilePane {
    //_______________________________________________ Parameters
    private static final String ID = "team-overview";
    private final Tournament tournament;

    //_______________________________________________ Initialize
    public TeamOverview(Tournament tournament) {
        setId(ID);
        this.tournament = tournament;

        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, () -> {}));
    }

    //_______________________________________________ Methods
    private void initUI() {
        for (Team team : tournament.getTeams()) {
            TeamView teamView = new TeamView(team);
            getChildren().add(teamView);
        }
    }

    private void onShow() {
        tournament.generateTeams();
        initUI();
    }
    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
