package com.hatit.visual.beige.tournament;

import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class EditTournamentView extends GridPane {
    //_______________________________________________ Parameters
    private static final String ID = "tournament-view";

    private final Tournament tournament;

    private final TextField nameField = new TextField();

    //_______________________________________________ Initialize
    public EditTournamentView(Tournament tournament) {
        setId(ID);
        this.tournament = tournament;
        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));

        initUI();
    }

    //_______________________________________________ Methods
    private void initUI() {
        Label nameFieldLabel = new Label("Name:");
        nameField.promptTextProperty().setValue("Bitte geben Sie einen Turniernamen ein!");

        add(nameFieldLabel, 0, 0);
        add(nameField, 1, 0);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.BASELINE);
        getRowConstraints().add(rowConstraints);
    }

    private void onShow() {
        nameField.textProperty().setValue(tournament.propName().get());
        tournament.propName().bind(nameField.textProperty());
    }

    private void onHide() {
        tournament.propName().unbind();
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
