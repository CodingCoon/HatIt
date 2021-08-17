package com.hatit.visual.beige.teams;

import com.hatit.data.team.Team;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class TeamOverview extends GridPane {
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
        getChildren().clear();
        int row = 0;
        int column = 0;

        for (Team team : tournament.getTeams()) {
            if (column == 5) {
                row++;
                column = 0;
            }

            TeamView teamView = new TeamView(team);
            add(teamView, column, row);
            column++;
        }
    }

    private void onShow() {
        tournament.generateTeams();
        initUI();
    }
    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
