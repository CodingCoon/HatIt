package com.hatit.visual.beige.teams;

import com.hatit.data.team.Team;
import com.hatit.data.team.ValuedPlayer;
import com.hatit.visual.StyleUtil;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class TeamView extends GridPane {

    //_______________________________________________ Parameters
    private static final String STYLE_CLASS = "team-view";
    private final Team team;

    //_______________________________________________ Initialize
    public TeamView(Team team) {
        getStyleClass().add(STYLE_CLASS);
        this.team = team;

        initUI();
    }

    //_______________________________________________ Methods
    private void initUI() {
        add(StyleUtil.h1(team.getName()), 0, 0, 2, 1);
        add(StyleUtil.h2("" + team.getFormattedTeamValue()), 2, 0);

        int row = 1;
        for (ValuedPlayer valuedPlayer : team.getPlayerList()) {
            add(StyleUtil.h2(valuedPlayer.getPlayer().getName()), 0, row);
            add(new Label("" + valuedPlayer.getTag()), 1, row);
            add(new Label("" + valuedPlayer.getFormattedValue()), 2, row);

            row++;
        }

        getColumnConstraints().add(new ColumnConstraints());
        getColumnConstraints().add(new ColumnConstraints());
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        getColumnConstraints().add(cc);
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
