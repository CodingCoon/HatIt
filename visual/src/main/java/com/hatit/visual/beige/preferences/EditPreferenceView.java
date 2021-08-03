package com.hatit.visual.beige.preferences;

import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.generation.Preferences;
import com.hatit.data.generation.Preferences.CriteriaUsage;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EditPreferenceView extends VBox {
    //_______________________________________________ Parameters
    private final Tournament tournament;

    private final TextField teamCountField = new TextField();

    private final List<Runnable> toRemoveListeners = new ArrayList<>();

    //_______________________________________________ Initialize
    public EditPreferenceView(Tournament tournament) {
        this.tournament = tournament;

        setSpacing(24);
        addPreferencesGrid();
        addTeamCount();

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
        preferencesGrid.setHgap(12);
        preferencesGrid.setVgap(12);
        int row = 0;

        preferencesGrid.add(StyleUtil.h2("Aktiv"), 0, row);
        preferencesGrid.add(StyleUtil.h2("Kriterium"), 1, row);
//        preferencesGrid.add(StyleUtil.h2("Range"), 2, row);
        preferencesGrid.add(StyleUtil.h2("Faktor"), 2, row);
        row++;

        Separator separator = new Separator();
        preferencesGrid.add(separator, 0, row, 3 ,1);
        row++;

        Preferences preferences = tournament.getPreferences();
        for (CriteriaUsage usage : preferences.getUsages()) {
            addUsageRow(preferencesGrid, row, usage);
            row++;
        }
        getChildren().add(preferencesGrid);
    }

    private void addUsageRow(GridPane preferencesGrid, int row, CriteriaUsage usage) {
        CheckBox activeCheckBox = createActiveCheckBox(usage);
        Label nameLabel = new Label(usage.getCriteriaName());
        Node factorField = createFactorField(usage);

        activeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            nameLabel.setDisable(! newValue);
            factorField.setDisable(! newValue);
        });

        preferencesGrid.add(activeCheckBox, 0, row);
        preferencesGrid.add(nameLabel, 1, row);
//            preferencesGrid.add(new Label(calcRange(usage)), 2, row);
        preferencesGrid.add(factorField, 2, row);
    }

    private Node createFactorField(CriteriaUsage usage) {
        if (usage.getCriteriaType() == CriteriaType.TAGGING) {
            return new Label("Aufteilend");
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
        HBox content = new HBox(new Label("Teamanzahl: "), teamCountField);
        content.setSpacing(8);
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
    private static final class StringToIntBinding extends IntegerBinding implements InvalidationListener {
        private final int defaultValue;
        private final StringProperty stringSource;

        private StringToIntBinding(int defaultValue, StringProperty stringSource) {
            this.defaultValue = defaultValue;
            this.stringSource = stringSource;
            this.stringSource.addListener(this);
        }

        @Override
        public void dispose() {
            this.stringSource.removeListener(this);
        }

        @Override
        public void invalidated(Observable observable) {
            invalidate();
        }

        @Override
        protected int computeValue() {
            String text = stringSource.get();
            if (text != null && ! text.isBlank()) {
                try {
                    return Integer.parseInt(text);
                }
                catch (NumberFormatException e) {
                    System.err.println("asdasd");
                    // some parts are not parseable as int, fallthrough to 0
                }
            }
            return defaultValue;
        }
    }

    //_______________________________________________ End
}
