package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.criteria.CriteriaView.TypeViewBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

class TaggingTypeContentView implements TypeViewBuilder {
    //_______________________________________________ Parameters
    private final TaggingSetting setting;
    private final ObservableList<String> options = FXCollections.observableArrayList();

    private final Label label = StyleUtil.createLabel("Optionen:");
    private final ListView<String> optionsListView = new ListView<>();

    //_______________________________________________ Initialize
    TaggingTypeContentView(Criteria criteria) {
        this.setting = (TaggingSetting) criteria.propSettings().get();

        setting.propOptions().forEach(option -> options.add(option.getOption()));
        optionsListView.setEditable(true);
        options.addListener(this::updateOptionList);
    }

    //_______________________________________________ Methods
    @Override
    public void addUI(GridPane pane) {
        pane.add(label, 0, 2);
        pane.add(optionsListView, 1, 2);
        GridPane.setValignment(label, VPos.TOP);
    }

    @Override
    public void removeUI(GridPane pane) {
        pane.getChildren().removeAll(label, optionsListView);
    }

    private void updateOptionList(ListChangeListener.Change<? extends String> c) {
        setting.propOptions().clear();
        options.forEach(o -> setting.propOptions().add(new TaggingSetting.Option(o)));
        // TODO:    think about changing tag on players set value
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
