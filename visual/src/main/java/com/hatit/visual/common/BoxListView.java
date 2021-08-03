package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BoxListView<T> extends VBox {
    //_______________________________________________ Parameters
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private static final String STYLE_CLASS = "box-list-view";

    private final ObservableList<T> content;
    private final Supplier<T> newItemSupplier;
    private final Function<T, Node> itemViewCreator;

    private final VBox itemBox = new VBox();
    private final ListChangeListener<T> contentChangeListener = this::onContentChanged;
    private final Map<T, Node> itemViewMapping = new HashMap<>();

    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    public BoxListView(ObservableList<T> content, Supplier<T> newItemSupplier, Function<T, Node> itemViewCreator) {
        getStyleClass().add(STYLE_CLASS);
        itemBox.getStyleClass().add(STYLE_CLASS);

        setSpacing(12);
        itemBox.setSpacing(12); // TODO: funktioniert warum auch immer nicht ohne die zwei Zeilen, Css sollte das abbilden

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
        getChildren().addAll(new AddItemButton(newItemSupplier), itemBox);
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
                    if (selectedItem.get().equals(removed)) {
                        selectedItem.set(null);
                    }
                    removeItemView(removed);
                }
            }
        }
    }

    //_______________________________________________ Inner CLasses
    private final class ItemView extends StackPane {
        private static final String STYLE_CLASS = "item-view";

        private ItemView(T item, Node content) {
            setPrefWidth(250);
            getStyleClass().add(STYLE_CLASS);
            getChildren().add(content);
            setOnMousePressed(event -> selectedItem.setValue(item));
        }
    }

    private final class AddItemButton extends StackPane {
        private static final String STYLE_CLASS = "add-button";

        private AddItemButton(Supplier<T> newItemSupplier) {
            getStyleClass().add(STYLE_CLASS);
            getChildren().add(new ImageView(ResourceUtil.ADD_ITEM));
            setOnMouseClicked(event -> content.add(newItemSupplier.get()));
        }
    }

    //_______________________________________________ End
}
