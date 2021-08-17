package com.hatit.visual.beige.player;

import com.hatit.data.criteria.ConstrainedRatingSetting;
import com.hatit.data.criteria.Criteria;
import com.hatit.data.player.Player;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

class ConstrainedRatingViewBuilder extends PlayerView.CriteriaViewBuilder<ConstrainedRatingSetting> {
    //_______________________________________________ Parameters
    private final Label label;
    private final Slider slider;
    //_______________________________________________ Initialize

    ConstrainedRatingViewBuilder(Criteria criteria) {
        super(criteria, (ConstrainedRatingSetting) criteria.propSettings().get());
        label = StyleUtil.createLabel(criteria.getName());

        ConstrainedRatingSetting.Range range = getSetting().getRange();
        slider = new Slider(range.getMin(), range.getMax(), range.getMin());
        slider.setBlockIncrement(1);
    }

    //_______________________________________________ Methods
    @Override
    void addUI(GridPane gridPane, int row) {
        gridPane.add(label, 0, row);
        gridPane.add(slider, 1, row);
    }

    @Override
    void activatePlayer(Player newPlayer) {
        IntegerProperty playerValueProperty = newPlayer.propQualitativStat(getCriteria());
        slider.valueProperty().setValue(playerValueProperty.get());
        playerValueProperty.bind(slider.valueProperty().asObject());
    }

    @Override
    void deactivatePlayer(Player oldPlayer) {
        oldPlayer.propQualitativStat(getCriteria()).unbind();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
