package com.hatit.visual.beige.player;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.OpenRatingSetting;
import com.hatit.data.player.Player;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.binding.StringToDoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class OpenRatingViewBuilder extends PlayerView.CriteriaViewBuilder<OpenRatingSetting> {
    //_______________________________________________ Parameters
    private final Label label;
    private final TextField valueField = new TextField();
    private final Label unitLabel;

    //_______________________________________________ Initialize
    protected OpenRatingViewBuilder(Criteria criteria) {
        super(criteria,  (OpenRatingSetting) criteria.propSettings().get());
        label = StyleUtil.createLabel(criteria.getName());
        unitLabel = StyleUtil.createLabel(getSetting().getUnit());
    }

    //_______________________________________________ Methods
    @Override
    void addUI(GridPane gridPane, int row) {
        gridPane.add(label, 0, row);
        gridPane.add(valueField, 1, row);
        gridPane.add(unitLabel, 2, row);
    }

    @Override
    protected void activatePlayer(Player newPlayer) {
        DoubleProperty playerValueProperty = newPlayer.propQualitativStat(getCriteria());
        valueField.textProperty().setValue("" + playerValueProperty.get());
        playerValueProperty.bind(new StringToDoubleBinding(valueField.textProperty()));
    }

    @Override
    protected void deactivatePlayer(Player oldPlayer) {
        oldPlayer.propQualitativStat(getCriteria()).unbind();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
