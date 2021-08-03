package com.hatit.visual.beige.teams;

import com.hatit.data.team.Team;
import com.hatit.data.team.ValuedPlayer;
import com.hatit.visual.StyleUtil;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TeamView extends GridPane {
    //_______________________________________________ Parameters
    private final Team team;

    //_______________________________________________ Initialize
    public TeamView(Team team) {
        this.team = team;

        initUI();
        setVgap(8);
        setHgap(8);
        setPadding(new Insets(12));
        setBorder(new Border(new BorderStroke(StyleUtil.ROSE, BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(1))));

        getColumnConstraints().add(new ColumnConstraints());
        getColumnConstraints().add(new ColumnConstraints());
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        getColumnConstraints().add(cc);
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

    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
