package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.OpenRatingSetting;
import com.hatit.visual.StyleUtil;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class OpenRatingViewBuilder implements CriteriaView.TypeViewBuilder{

    //_______________________________________________ Parameters
    private final OpenRatingSetting setting;

    private final Label unitLabel = StyleUtil.createLabel("Einheit:");
    private final TextField unitField = new TextField();

    //_______________________________________________ Initialize
    OpenRatingViewBuilder(Criteria criteria) {
        this.setting = (OpenRatingSetting) criteria.propSetting().get();

        unitField.setText(setting.getUnit());
        unitField.textProperty().addListener((observable, oldValue, newValue) -> setting.setUnit(newValue));
    }

    //_______________________________________________ Methods
    @Override
    public void addUI(GridPane pane) {
        pane.add(unitLabel, 0, 2);
        pane.add(unitField, 1, 2);
    }

    @Override
    public void removeUI(GridPane pane) {
        pane.getChildren().removeAll(unitLabel, unitField);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
