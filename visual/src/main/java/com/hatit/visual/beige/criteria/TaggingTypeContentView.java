package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.TaggingSetting;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class TaggingTypeContentView extends VBox {
    //_______________________________________________ Parameters
    private final TaggingSetting setting;

    private final ObservableList<String> options = FXCollections.observableArrayList();
    private final ListView<String> optionsListView = new ListView<>();

    //_______________________________________________ Initialize
    public TaggingTypeContentView(Criteria criteria) {
        this.setting = (TaggingSetting) criteria.propSettings().get();
        initUI();

        setting.propOptions().forEach(option -> options.add(option.getOption()));
        optionsListView.setEditable(true);
        options.addListener(this::updateOptionList);
    }

    private void updateOptionList(ListChangeListener.Change<? extends String> c) {
        setting.propOptions().clear();
        options.forEach(o -> setting.propOptions().add(new TaggingSetting.Option(o, "")));
        // TODO:    think about changing tag on players set value
    }

    //_______________________________________________ Methods
    private void initUI() {
        setSpacing(12);
        getChildren().addAll(
                new Label("Optionen:"),
                optionsListView);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
