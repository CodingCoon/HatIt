package com.hatit.visual.beige.player;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.data.player.Player;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.player.PlayerView.CriteriaViewBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

class TaggingViewBuilder extends CriteriaViewBuilder<TaggingSetting> {

    //_______________________________________________ Parameters
    private static final String FOLLOWING = "following";

    private final ToggleGroup group;

    private final StringProperty selectedOption = new SimpleStringProperty();
    private final List<ToggleButton> radioButtons = new ArrayList<>();

    private final Label label;
    private final VBox radioButtonBox = new VBox();

    //_______________________________________________ Initialize
    TaggingViewBuilder(Criteria criteria) {
        super(criteria, (TaggingSetting) criteria.propSetting().get());
        this.group = new ToggleGroup();

        label = StyleUtil.createLabel(getCriteria().getName());
    }

    //_______________________________________________ Methods
    @Override
    void addUI(GridPane gridPane, int row) {
        for (TaggingSetting.Option option : getSetting().propOptions()) {
            ToggleButton button = new ToggleButton(option.getOption()) {
                @Override
                public void fire() {
                    if (! isSelected()) {
                        super.fire();
                    }
                }
            };
            button.setMaxWidth(Double.MAX_VALUE);
            button.setToggleGroup(group);
            button.setUserData(option.getOption());
            if (! radioButtons.isEmpty()) {
                button.getStyleClass().add(FOLLOWING);
            }
            radioButtons.add(button);
            radioButtonBox.getChildren().add(button);
        }
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateSelectedOption(newValue));

        gridPane.add(label, 0, row);
        gridPane.add(radioButtonBox, 1, row);
        GridPane.setValignment(label, VPos.TOP);
    }

    private void updateSelectedOption(Toggle toggled) {
        if (toggled == null) {
//            getCriteria().getDefaultValueProperty().getValue();
        }
        selectedOption.set((String) toggled.getUserData());
    }

    @Override
    void activatePlayer(Player newPlayer) {

        StringProperty playerValueProperty = newPlayer.propTaggingStat(getCriteria());
        for (ToggleButton toggle : radioButtons) {
            if (toggle.getText().equals(playerValueProperty.get())) {
                group.selectToggle(toggle);
            }
        }
        playerValueProperty.bind(selectedOption);
    }

    @Override
    void deactivatePlayer(Player oldPlayer) {
        oldPlayer.propTaggingStat(getCriteria()).unbind();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
