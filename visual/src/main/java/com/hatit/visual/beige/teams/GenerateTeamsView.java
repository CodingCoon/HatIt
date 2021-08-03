package com.hatit.visual.beige.teams;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.generation.Preferences;
import com.hatit.data.tournament.Tournament;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GenerateTeamsView extends VBox {
    //_______________________________________________ Parameters
    private final Tournament tournament;

    //_______________________________________________ Initialize
    public GenerateTeamsView(Tournament tournament) {
        this.tournament = tournament;

        List<Criteria> taggingCriteria = new ArrayList<>();
        for (Preferences.CriteriaUsage usage : tournament.getPreferences().getUsages()) {
            if (usage.isActive() && usage.getCriteriaType() == CriteriaType.TAGGING) {
                taggingCriteria.add(usage.getCriteria());
            }
        }

        initUI();
    }

    //_______________________________________________ Methods
    private void initUI() {
        Label startLabel = new Label("Es werden nun");
        Label playerLabel = new Label(tournament.propPlayers().size() + " Spieler");
        Label groupsLabel = new Label("in x Gruppen eingeteilt");
        Label teamsLabel = new Label("die gleichmäßig auf " + tournament.getPreferences().propTeamCount().get() + " Teams aufgeteilt werden");
        VBox generationText = new VBox(startLabel, playerLabel, groupsLabel, teamsLabel);

        Button startButton = new Button("Teams generieren");
        startButton.setOnAction(event -> tournament.generateTeams());

        setSpacing(12);
        getChildren().addAll(generationText, startButton);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
