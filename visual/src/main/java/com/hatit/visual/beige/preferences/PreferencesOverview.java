package com.hatit.visual.beige.preferences;

import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.generation.Preferences;
import com.hatit.data.generation.Preferences.CriteriaUsage;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.binding.StringToIntBinding;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PreferencesOverview extends VBox {
    //_______________________________________________ Parameters
    private static final String ID = "preferences-overview";

    private final Tournament tournament;

    private final TextField teamCountField = new TextField();
    private final List<Runnable> toRemoveListeners = new ArrayList<>();

    //_______________________________________________ Initialize
    public PreferencesOverview(Tournament tournament) {
        this.tournament = tournament;
        setId(ID);

        addTeamCount();
        getChildren().add(new Separator());
        addPreferencesGrid();
        getChildren().add(new Separator());

        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
    }

    //_______________________________________________ Methods
    private void onShow() {
        Preferences preferences = tournament.getPreferences();
        teamCountField.setText("" + preferences.propTeamCount().get());
        preferences.propTeamCount().bind(new StringToIntBinding(2, teamCountField.textProperty()));
    }

    private void onHide() {
        toRemoveListeners.forEach(Runnable::run);
        tournament.getPreferences().propTeamCount().unbind();
    }

    private void addPreferencesGrid() {
        GridPane preferencesGrid = new GridPane();
        int row = 0;

        Preferences preferences = tournament.getPreferences();
        for (CriteriaUsage usage : preferences.getUsages()) {
            addUsageRow(preferencesGrid, row, usage);
            row++;
        }
        getChildren().add(preferencesGrid);
    }

    private void addUsageRow(GridPane preferencesGrid, int row, CriteriaUsage usage) {
        CheckBox activeCheckBox = createActiveCheckBox(usage);
        Label nameLabel = StyleUtil.createLabel(usage.getCriteriaName());
        Node factorField = createFactorField(usage);

        activeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            nameLabel.setDisable(! newValue);
            factorField.setDisable(! newValue);
            if (factorField instanceof ComboBox) { // TODO:
                factorField.setDisable(true);
            }
        });

        preferencesGrid.add(activeCheckBox, 0, row);
        preferencesGrid.add(nameLabel, 1, row);
        preferencesGrid.add(factorField, 2, row);
    }

    private Node createFactorField(CriteriaUsage usage) {
        if (usage.getCriteriaType() == CriteriaType.TAGGING) {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setValue("Aufteilend");
            comboBox.setDisable(true);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            return comboBox;
        }
        else {
            TextField factorField = new TextField("" + usage.propFactor().get());
            ChangeListener<? super String> changeListener = (__, ___, newValue) -> usage.propFactor().setValue(toDouble(newValue));
            factorField.textProperty().addListener(changeListener);
            toRemoveListeners.add(() -> factorField.textProperty().removeListener(changeListener));
            return factorField;
        }
    }

    private CheckBox createActiveCheckBox(CriteriaUsage usages) {
        CheckBox activeBox = new CheckBox();
        activeBox.selectedProperty().set(usages.propActive().getValue());
        usages.propActive().bind(activeBox.selectedProperty());
        toRemoveListeners.add(() -> usages.propActive().unbind());
        return activeBox;
    }

    private void addTeamCount() {
        HBox content = new HBox(StyleUtil.createLabel("Teamanzahl: "), teamCountField);
        getChildren().add(content);
    }

    private Double toDouble(String text) {
        try {
            return Double.parseDouble(text);
        }
        catch (NumberFormatException e) {
            return 1d;
        }
    }

    //_______________________________________________ Inner Classes



    /*
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




     */
    //_______________________________________________ End
}
