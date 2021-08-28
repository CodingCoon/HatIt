package com.hatit.visual.beige.player;

import com.hatit.data.criteria.ConstrainedRatingSetting;
import com.hatit.data.criteria.Criteria;
import com.hatit.data.player.Player;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.common.RatingView;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

class ConstrainedRatingViewBuilder extends PlayerView.CriteriaViewBuilder<ConstrainedRatingSetting> {
    //_______________________________________________ Parameters
    private final Label label;
    private final RatingView ratingView;
    //_______________________________________________ Initialize

    ConstrainedRatingViewBuilder(Criteria criteria) {
        super(criteria, (ConstrainedRatingSetting) criteria.propSetting().get());
        label = StyleUtil.createLabel(criteria.getName());

        ConstrainedRatingSetting.Range range = getSetting().getRange();
        ratingView = new RatingView(range.getMax());
    }

    //_______________________________________________ Methods
    @Override
    void addUI(GridPane gridPane, int row) {
        gridPane.add(label, 0, row);
        gridPane.add(ratingView, 1, row);
    }

    @Override
    void activatePlayer(Player newPlayer) {
        IntegerProperty playerValueProperty = newPlayer.propQualitativStat(getCriteria());
        ratingView.propValue().setValue(playerValueProperty.get());
        playerValueProperty.bind(ratingView.propValue());
    }

    @Override
    void deactivatePlayer(Player oldPlayer) {
        oldPlayer.propQualitativStat(getCriteria()).unbind();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
