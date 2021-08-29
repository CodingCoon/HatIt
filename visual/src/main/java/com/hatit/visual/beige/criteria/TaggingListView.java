package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.TaggingSetting;
import com.hatit.data.criteria.TaggingSetting.Option;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class TaggingListView extends VBox {
    //_______________________________________________ Parameters
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private static final String TAG_LIST_STYLE_CLASS = "tag-list";
    private static final String TAG_BOX_STYLE_CLASS = "tag-box";

    private final ObservableList<Option> content;
    private final Supplier<Option> newItemSupplier = () -> new TaggingSetting.Option("");
    private final Function<Option, Node> itemViewCreator = OptionBox::new; // TODO: abstract class or cleanup

    private final VBox itemBox = new VBox();
    private final ListChangeListener<Option> contentChangeListener = this::onContentChanged;
    private final Map<Option, Node> itemViewMapping = new HashMap<>();

    private final ObjectProperty<Option> selectedItem = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    public TaggingListView(ObservableList<Option> content) {
        getStyleClass().add(TAG_LIST_STYLE_CLASS);
        itemBox.getStyleClass().add(TAG_BOX_STYLE_CLASS);

        this.content = content;

        initUI();
        selectedItem.addListener((observable, oldValue, newValue) -> updateItemView(oldValue, newValue));
        sceneProperty().addListener(new ScenePartChangeListener(this::activate, this::deactivate));
    }

    //_______________________________________________ Methods
    private void activate() {
        content.addListener(contentChangeListener);
    }

    private void deactivate() {
        content.removeListener(contentChangeListener);
    }

    private void updateItemView(Option oldValue, Option newValue) {
        if (oldValue != null) {
            itemViewMapping.get(oldValue).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
        }
        if (newValue != null) {
            itemViewMapping.get(newValue).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
        }
    }

    private void initUI() {
        getChildren().addAll(new AddItemButton(newItemSupplier), new ScrollPane(itemBox));
        for (Option item : content) {
            addItemView(item);
        }
    }

    private void addItemView(Option item) {
        Node content = itemViewCreator.apply(item);
        ItemView itemView = new ItemView(item, content);
        itemBox.getChildren().add(itemView);
        itemViewMapping.put(item, itemView);
    }

    private void removeItemView(Option removed) {
        Node itemViewToRemove = itemViewMapping.remove(removed);
        itemBox.getChildren().remove(itemViewToRemove);
    }

    private void onContentChanged(ListChangeListener.Change<? extends Option> c) {
        while (c.next()) {

            if (c.wasAdded()) {
                for (Option added : c.getAddedSubList()) {
                    addItemView(added);
                    selectedItem.setValue(added);
                }
            }
            else if (c.wasRemoved()) {
                for (Option removed : c.getRemoved()) {
                    Option currentSelectedItem = selectedItem.get();
                    if (currentSelectedItem != null && currentSelectedItem.equals(removed)) {
                        selectedItem.set(null);
                    }
                    removeItemView(removed);
                }
            }
        }
    }

    //_______________________________________________ Inner CLasses
    private class OptionBox extends VBox {
        private static final String STYLE_CLASS = "option-box"; // TODO: namings?
        private final TaggingSetting.Option option;

        private final TextField textField = new TextField();
        private final Label label     = new Label();
        private final ChangeListener<Option> optionChangeListener = (observable, oldValue, newValue) -> updateUI(oldValue, newValue);

        private OptionBox(TaggingSetting.Option option) {
            getStyleClass().add(STYLE_CLASS);
            this.option = option;
            this.textField.setText(option.getOption());
            this.option.propOption().bind(textField.textProperty());
            this.label.textProperty().bind(option.propOption());
            sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
            updateUI(option, null);
        }

        private void onShow() {
            selectedItem.addListener(optionChangeListener);
        }

        private void onHide() {
            selectedItem.removeListener(optionChangeListener);
        }

        private void updateUI(TaggingSetting.Option oldValue, TaggingSetting.Option newValue) {
            if (oldValue == option) {
                getChildren().remove(textField);
                getChildren().add(label);
            }

            if (newValue == option) {
                getChildren().remove(label);
                getChildren().add(textField);
                Platform.runLater(() -> {
                    textField.requestFocus();
                    textField.positionCaret(textField.getLength());
                });
            }
        }
    }

    private final class ItemView extends HBox {
        private static final String STYLE_CLASS = "tag-view";
        private final ImageView deleteButton  = new ImageView(ResourceUtil.DELETE);

        private final Timeline showTimeLine = createTimeline(1);
        private final Timeline hideTimeLine = createTimeline(0);
        private final Option item;

        private ItemView(Option item, Node contentView) {
            this.item = item;
            setPrefWidth(250);
            VBox buttons = new VBox(deleteButton);
            buttons.setSpacing(4);
            getStyleClass().add(STYLE_CLASS);
            getChildren().addAll(contentView, buttons);
            setHgrow(contentView, Priority.ALWAYS);

            setOnMousePressed(event -> selectedItem.setValue(item));

            deleteButton.setEffect(StyleUtil.createColorAdjust(StyleUtil.LIGHT));
            deleteButton.hoverProperty().addListener((observable, oldValue, newValue) -> updateDeleteButtonColor(newValue));
            deleteButton.setOnMouseClicked(event -> content.remove(item));
            deleteButton.opacityProperty().setValue(0);
            setOnMouseEntered(event -> showTimeLine.playFromStart());
            setOnMouseExited(event -> hideTimeLine.playFromStart());
        }

        private void updateDeleteButtonColor(Boolean isHovering) {
            Effect effect = isHovering ? StyleUtil.createColorAdjust(StyleUtil.BLUE)    // TODO:
                                       : StyleUtil.createColorAdjust(StyleUtil.QUEEN_BLUE);
            deleteButton.setEffect(effect);
        }

        private Timeline createTimeline(double endValue) {
            KeyValue dkv = new KeyValue(deleteButton.opacityProperty(), endValue);
            KeyFrame keyFrame  = new KeyFrame(Duration.millis(200), dkv);
            return new Timeline(keyFrame);
        }
    }

    private final class AddItemButton extends StackPane {
        private static final String STYLE_CLASS = "add-button";
        private static final double MIN_WIDTH = 250d;

        private AddItemButton(Supplier<Option> newItemSupplier) {
            getStyleClass().add(STYLE_CLASS);
            setPrefWidth(MIN_WIDTH);
            getChildren().add(new ImageView(ResourceUtil.ADD_TAG));
            setOnMouseClicked(event -> content.add(newItemSupplier.get()));
        }
    }

    //_______________________________________________ End
}
