package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
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

public class BoxListView<T> extends VBox {
    //_______________________________________________ Parameters
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private static final String BOX_LIST_VIEW_STYLE_CLASS = "box-list-view";
    private static final String ITEM_BOX_STYLE_CLASS = "item-box";

    private final ObservableList<T> content;
    private final Image addImage;
    private final Supplier<T> newItemSupplier;
    private final Function<T, Node> itemViewCreator;

    private final VBox itemBox = new VBox();
    private final ListChangeListener<T> contentChangeListener = this::onContentChanged;
    private final Map<T, Node> itemViewMapping = new HashMap<>();

    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    public BoxListView(ObservableList<T> content, Image addImage, Supplier<T> newItemSupplier, Function<T, Node> itemViewCreator) {
        this(BOX_LIST_VIEW_STYLE_CLASS, content, addImage,newItemSupplier, itemViewCreator);
    }

    public BoxListView(String id, ObservableList<T> content, Image addImage, Supplier<T> newItemSupplier, Function<T, Node> itemViewCreator) {
        getStyleClass().add(id);
        itemBox.getStyleClass().add(ITEM_BOX_STYLE_CLASS);
        this.addImage = addImage;

        this.content = content;
        this.newItemSupplier = newItemSupplier;
        this.itemViewCreator = itemViewCreator;

        initUI();
        selectedItem.addListener((observable, oldValue, newValue) -> updateItemView(oldValue, newValue));
        sceneProperty().addListener(new ScenePartChangeListener(this::activate, this::deactivate));
    }

    //_______________________________________________ Methods
    public ReadOnlyObjectProperty<T> propSelectedItem() {
        return selectedItem;
    }

    private void activate() {
        content.addListener(contentChangeListener);
    }

    private void deactivate() {
        content.removeListener(contentChangeListener);
    }

    private void updateItemView(T oldValue, T newValue) {
        if (oldValue != null) {
            itemViewMapping.get(oldValue).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
        }
        if (newValue != null) {
            itemViewMapping.get(newValue).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
        }
    }

    private void initUI() {
        getChildren().addAll(new AddItemButton(newItemSupplier), new ScrollPane(itemBox));
        for (T item : content) {
            addItemView(item);
        }
    }

    private void addItemView(T item) {
        Node content = itemViewCreator.apply(item);
        ItemView itemView = new ItemView(item, content);
        itemBox.getChildren().add(itemView);
        itemViewMapping.put(item, itemView);
    }

    private void removeItemView(T removed) {
        Node itemViewToRemove = itemViewMapping.remove(removed);
        itemBox.getChildren().remove(itemViewToRemove);
    }

    private void onContentChanged(ListChangeListener.Change<? extends T> c) {
        while (c.next()) {

            if (c.wasAdded()) {
                for (T added : c.getAddedSubList()) {
                    addItemView(added);
                    selectedItem.setValue(added);
                }
            }
            else if (c.wasRemoved()) {
                for (T removed : c.getRemoved()) {
                    T currentSelectedItem = selectedItem.get();
                    if (currentSelectedItem != null && currentSelectedItem.equals(removed)) {
                        selectedItem.set(null);
                    }
                    removeItemView(removed);
                }
            }
        }
    }

    //_______________________________________________ Inner CLasses
    private final class ItemView extends HBox {
        private static final String STYLE_CLASS = "item-view";
        private final ImageView deleteButton  = new ImageView(ResourceUtil.DELETE);

        private final Timeline showTimeLine = createTimeline(1);
        private final Timeline hideTimeLine = createTimeline(0);
        private final T item;

        private ItemView(T item, Node contentView) {
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

        private boolean isSelected() {
            return selectedItem.get() == item;
        }

        private void updateDeleteButtonColor(Boolean isHovering) {
            Effect effect;
            // TODO:
            if (isSelected()) {
                 effect = isHovering ? StyleUtil.createColorAdjust(StyleUtil.LIGHT)
                                     : StyleUtil.createColorAdjust(StyleUtil.LIGHT_ROSE);
            }
            else {
                effect = isHovering ? StyleUtil.createColorAdjust(StyleUtil.ROSE)
                                    : StyleUtil.createColorAdjust(StyleUtil.LIGHT);
            }
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

        private AddItemButton(Supplier<T> newItemSupplier) {
            getStyleClass().add(STYLE_CLASS);
            setPrefWidth(MIN_WIDTH);
            getChildren().add(new ImageView(addImage));
            setOnMouseClicked(event -> content.add(newItemSupplier.get()));
        }
    }

    //_______________________________________________ End
}
