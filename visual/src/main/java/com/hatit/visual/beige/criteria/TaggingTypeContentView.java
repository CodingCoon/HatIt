package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.criteria.CriteriaView.TypeViewBuilder;
import com.hatit.visual.common.BoxListView;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class TaggingTypeContentView implements TypeViewBuilder {
    //_______________________________________________ Parameters
    private static final String OPTION_LIST_ID = "option-list";

    private final Label label = StyleUtil.createLabel("Optionen:");
    private final BoxListView<TaggingSetting.Option> optionsListView;

    //_______________________________________________ Initialize
    TaggingTypeContentView(Criteria criteria) {
        TaggingSetting setting = (TaggingSetting) criteria.propSetting().get();
        optionsListView = new BoxListView<>(setting.propOptions(), ResourceUtil.ADD_TAG, () -> new TaggingSetting.Option(""), OptionBox::new);
        optionsListView.setId(OPTION_LIST_ID);
    }

    //_______________________________________________ Methods
    @Override
    public void addUI(GridPane pane) {
        pane.add (label, 0, 2);
        pane.add(optionsListView, 1, 2);
        GridPane.setValignment(label, VPos.TOP);
    }

    @Override
    public void removeUI(GridPane pane) {
        pane.getChildren().removeAll(label, optionsListView);
    }

    //_______________________________________________ Inner Classes
    private class OptionBox extends VBox {
        private final TaggingSetting.Option option;

        private final TextField textField = new TextField();
        private final Label     label     = new Label();
        private final ChangeListener<TaggingSetting.Option> optionChangeListener = (observable, oldValue, newValue) -> updateUI(oldValue, newValue);

        private OptionBox(TaggingSetting.Option option) {
            this.option = option;
            this.textField.setText(option.getOption());
            this.option.propOption().bind(textField.textProperty());
            this.label.textProperty().bind(option.propOption());
            sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
            updateUI(option, null);
        }

        private void onShow() {
            optionsListView.propSelectedItem().addListener(optionChangeListener);
        }

        private void onHide() {
            optionsListView.propSelectedItem().removeListener(optionChangeListener);
        }

        private void updateUI(TaggingSetting.Option oldValue, TaggingSetting.Option newValue) {
            if (oldValue == option) {
                getChildren().remove(textField);
                getChildren().add(label);
            }

            if (newValue == option) {
                getChildren().remove(label);
                getChildren().add(textField);
                textField.requestFocus();
            }
        }
    }

    //_______________________________________________ End
}
