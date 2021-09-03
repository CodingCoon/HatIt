package com.hatit.visual.rose;

import com.hatit.data.tournament.Tournament;
import com.hatit.visual.Enviroment;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class RoseView extends HBox {
    //_______________________________________________ Parameters
    private static final String ID = "rose-view";

    private final Label tournamentLabel = new Label("Willkommen");

    //_______________________________________________ Initialize
    public RoseView(Enviroment enviroment) {
        setId(ID);
        initUI();

        enviroment.propCurrentTournament().addListener((observable, oldValue, newValue) -> selectedTournamentChanged(oldValue, newValue));
        selectedTournamentChanged(null, enviroment.propCurrentTournament().get());
    }

    //_______________________________________________ Methods
    private void initUI() {
        tournamentLabel.setId("title-label");

        StackPane labelPane = new StackPane(tournamentLabel);
//        Button settingsButton = StyleUtil.createImageButton(ResourceUtil.SETTINGS_1, "Öffnet die Einstellungen", event -> openSettingsDialog());
        Button escapeButton   = StyleUtil.createImageButton(ResourceUtil.POWER, "Beendet die Anwendung", event -> closeApplication());

        getChildren().addAll(labelPane/*, settingsButton*/, escapeButton);
        setHgrow(labelPane, Priority.ALWAYS);
    }

    private void closeApplication() {
//        save(); // TODO:
        // ask for really?
        System.exit(0);
    }

    private void openSettingsDialog() {
        // TODO:
    }

    private void selectedTournamentChanged(Tournament oldValue, Tournament newValue) {
        if (oldValue != null) {
            tournamentLabel.textProperty().unbind();
            tournamentLabel.setText("Willkommen");
        }
        if (newValue != null) {
            tournamentLabel.textProperty().bind(newValue.propName());
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}