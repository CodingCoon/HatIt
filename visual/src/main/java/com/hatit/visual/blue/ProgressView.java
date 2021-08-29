package com.hatit.visual.blue;

import com.hatit.data.tournament.Tournament;
import com.hatit.data.tournament.TournamentState;
import com.hatit.io.IOService;
import com.hatit.visual.Enviroment;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProgressView extends VBox {
    //_______________________________________________ Parameters
    private static final String STYLE_CLASS = "progress-view";
    private final Enviroment enviroment;
    private final Tournament tournament;

    //_______________________________________________ Initialize
    public ProgressView(Enviroment enviroment) {
        getStyleClass().add(STYLE_CLASS);
        this.enviroment = enviroment;
        this.tournament = enviroment.getCurrentTournament();

        VBox progressView = new VBox();
        initUI(progressView);
        initProgressView(progressView);
        tournament.getState().addListener((observable, oldValue, newValue) -> initProgressView(progressView));
    }

    //_______________________________________________ Methods
    private void initUI(VBox progressView) {
        Button backButton = StyleUtil.createTextedImageButton(ResourceUtil.PREVIOUS, "Zurück", event -> goBack());
        Button saveButton = StyleUtil.createTextedImageButton(ResourceUtil.SAVE, "Speichern", event -> store());
        backButton.setMaxWidth(Double.MAX_VALUE); // TODO: not so nice, but extends it without affecting other buttons

        getChildren().addAll(progressView, saveButton, backButton);
        setVgrow(progressView, Priority.ALWAYS);
        setAlignment(Pos.TOP_RIGHT);
    }

    private void store() {
        IOService.createService().store(tournament);
    }

    private void goBack() {
        if (tournament.getState().get().hasPrevious()) {
            tournament.previousState();
        }
        else {
            enviroment.propCurrentTournament().set(null);
        }
    }

    private void initProgressView(VBox progressView) {  // TODO: to breadcrumb view
        progressView.getChildren().clear();
        TournamentState currentState = tournament.getState().get();

        for (TournamentState tournamentState : TournamentState.values()) {
            Label label = new Label(tournamentState.getLabel());
            label.getStyleClass().add("state-label");

            if (currentState == tournamentState) {
                progressView.getChildren().add(new Separator(Orientation.HORIZONTAL));
                progressView.getChildren().add(label);
                progressView.getChildren().add(new Separator(Orientation.HORIZONTAL));
            }
            else if (tournamentState.ordinal() > currentState.ordinal()) {
                label.getStyleClass().add("coming");
                progressView.getChildren().add(label);
            }
            else {
                label.getStyleClass().add("done");
                progressView.getChildren().add(label);
            }
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
