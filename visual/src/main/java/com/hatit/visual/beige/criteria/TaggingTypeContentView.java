package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.criteria.CriteriaView.TypeViewBuilder;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

class TaggingTypeContentView implements TypeViewBuilder {
    //_______________________________________________ Parameters
    private final Label label = StyleUtil.createLabel("Optionen:");
    private final Node optionListView;

    //_______________________________________________ Initialize
    TaggingTypeContentView(Criteria criteria) {
        TaggingSetting setting = (TaggingSetting) criteria.propSetting().get();
        optionListView = new TaggingListView(setting.propOptions());
    }

    //_______________________________________________ Methods
    @Override
    public void addUI(GridPane pane) {
        pane.add (label, 0, 2);
        pane.add(optionListView, 1, 2);
        GridPane.setValignment(label, VPos.TOP);
    }

    @Override
    public void removeUI(GridPane pane) {
        pane.getChildren().removeAll(label, optionListView);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
