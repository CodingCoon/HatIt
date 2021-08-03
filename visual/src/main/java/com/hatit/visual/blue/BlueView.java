package com.hatit.visual.blue;

import com.hatit.data.tournament.Tournament;
import com.hatit.visual.Enviroment;
import javafx.scene.layout.StackPane;

public class BlueView extends StackPane {
    //_______________________________________________ Parameters
    private static final String ID = "blue-view";

    private final Enviroment enviroment;

    //_______________________________________________ Initialize
    //_______________________________________________ Initialize
    public BlueView(Enviroment enviroment) {
        setId(ID);
        this.enviroment = enviroment;

        enviroment.propCurrentTournament().addListener((observable, oldValue, newValue) -> selectionChanged(newValue));
        selectionChanged(enviroment.getCurrentTournament());
    }

    //_______________________________________________ Methods
    private void selectionChanged(Tournament currentTournament) {
        getChildren().clear();

        if (currentTournament != null) {
            getChildren().add(new ProgressView(enviroment));
        }
        else {
            getChildren().add(new ExplorerView(enviroment));
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
