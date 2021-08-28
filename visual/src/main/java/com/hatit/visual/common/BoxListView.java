package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
    private static final String STYLE_CLASS = "box-list-view";

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
        getStyleClass().add(STYLE_CLASS);
        itemBox.getStyleClass().add(STYLE_CLASS);
        this.addImage = addImage;

        this.content = content;
        this.newItemSupplier = newItemSupplier;
        this.itemViewCreator = itemViewCreator;

        initUI();
        selectedItem.addListener((observable, oldValue, newValue) -> updateItemView(oldValue, newValue));
        sceneProperty().addListener(new ScenePartChangeListener(this::activate, this::deactivate));

//        itemBox.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
//            @Override
//            public void handle(MouseDragEvent event) {
//                removePreview(itemBox);
//                int indexOfDraggingNode = itemBox.getChildren().indexOf(event.getGestureSource());
//                rotateNodes(itemBox, indexOfDraggingNode, itemBox.getChildren().size()-1);
//            }
//        });
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


//    private void addPreview(final VBox root, ItemView itemView) {
//        ImageView imageView = new ImageView(itemView.snapshot(null, null));
//        imageView.setManaged(false);
//        imageView.setMouseTransparent(true);
//        root.getChildren().add(imageView);
//        root.setUserData(imageView);
//        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                imageView.relocate(event.getX(), event.getY());
//            }
//        });
//    }

//    private void removePreview(final VBox root) {
//        root.setOnMouseDragged(null);
//        root.getChildren().remove(root.getUserData());
//        root.setUserData(null);
//    }
//
//
//    private void rotateNodes(final VBox root, final int indexOfDraggingNode,
//                             final int indexOfDropTarget) {
//        if (indexOfDraggingNode >= 0 && indexOfDropTarget >= 0) {
//            final Node node = root.getChildren().remove(indexOfDraggingNode);
//            root.getChildren().add(indexOfDropTarget, node);
//        }
//    }


    //_______________________________________________ Inner CLasses
    private final class ItemView extends HBox {
        private static final String STYLE_CLASS = "item-view";
        private final Button deleteButton  = new Button("D");
        // private final Button moveButton    = new Button("M");

        private final Timeline showTimeLine = createTimeline(1);
        private final Timeline hideTimeLine = createTimeline(0);

        private ItemView(T item, Node contentView) {
            setPrefWidth(250);
            VBox buttons = new VBox(deleteButton/*, moveButton*/);
            buttons.setSpacing(4);
            getStyleClass().add(STYLE_CLASS);
            getChildren().addAll(contentView, buttons);
            setHgrow(contentView, Priority.ALWAYS);

            setOnMousePressed(event -> selectedItem.setValue(item));

            deleteButton.setOnAction(event -> content.remove(item));
            deleteButton.opacityProperty().setValue(0);
//            moveButton.opacityProperty().setValue(0);
            setOnMouseEntered(event -> showTimeLine.playFromStart());
            setOnMouseExited(event -> hideTimeLine.playFromStart());

//            setOnDragDetected(event -> {
//                addPreview(itemBox, ItemView.this);
//                startFullDrag();
//            });
//            // next two handlers just an idea how to show the drop target visually:
//            setOnMouseDragEntered(event -> setStyle("-fx-background-color: #ffffa0;"));
//            setOnMouseDragExited(event -> setStyle(""));
//
//            setOnMouseDragReleased(event -> {
//                removePreview(itemBox);
//                setStyle("");
//                int indexOfDraggingNode = itemBox.getChildren().indexOf(event.getGestureSource());
//                int indexOfDropTarget = itemBox.getChildren().indexOf(ItemView.this);
//                rotateNodes(itemBox, indexOfDraggingNode, indexOfDropTarget);
//                event.consume();
//            });
        }

        private Timeline createTimeline(double endValue) {
            KeyValue dkv = new KeyValue(deleteButton.opacityProperty(), endValue);
//            KeyValue mkv = new KeyValue(moveButton.opacityProperty(), endValue);

            KeyFrame keyFrame  = new KeyFrame(Duration.millis(200), dkv/*, mkv*/);
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
