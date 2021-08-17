package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.CacheHint;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class CriteriaView extends GridPane {
    //_______________________________________________ Parameters
    private static final String ID = "criteria-view";

    private static final StringConverter<CriteriaType> CRITERIA_TYPE_TO_STRING = new StringConverter<>() {
        @Override
        public String toString(CriteriaType object) {

            return object == null ? "" : object.getName();
        }

        @Override
        public CriteriaType fromString(String string) {
            return CriteriaType.valueOf(string);
        }
    };

    private static final Map<CriteriaType, Function<Criteria, CriteriaView.TypeViewBuilder>> TYPE_CONTENT_MAPPING = new HashMap<>();
    static {
        TYPE_CONTENT_MAPPING.put(null, criteria -> null);
        TYPE_CONTENT_MAPPING.put(CriteriaType.TAGGING           , TaggingTypeContentView::new);
        TYPE_CONTENT_MAPPING.put(CriteriaType.CONSTRAINED_RATING, ConstrainedRatingViewBuilder::new);
        TYPE_CONTENT_MAPPING.put(CriteriaType.OPEN_RATING       , OpenRatingViewBuilder::new);
    }

    private final ReadOnlyObjectProperty<Criteria> selectedCriteria;

    private final TextField nameField = new TextField("Name");
    private final ComboBox<CriteriaType> criteriaTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(CriteriaType.values())) {
        @Override
        public void show() {
            super.show(); // TODO:  move popup down
        }
    };

    private final ObjectProperty<CriteriaView.TypeViewBuilder> currentTypeViewBuilder = new SimpleObjectProperty<>();

    private final ChangeListener<Criteria> selectedCriteriaChanged = (observable, oldValue, newValue) -> updateContent(oldValue, newValue);
    private final ChangeListener<CriteriaType> typeChangedListener = (observable, oldValue, newValue) -> updateTypeContent(newValue);


    //_______________________________________________ Initialize
    CriteriaView(ReadOnlyObjectProperty<Criteria> selectedCriteria) {
        setId(ID);
        this.selectedCriteria = selectedCriteria;
        initUI();

        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
    }

    //_______________________________________________ Methods
    private void initUI() {
        add(StyleUtil.createLabel("Kriteriumname:"), 0, 0);
        add(nameField, 1, 0);

        add(StyleUtil.createLabel("Typ:"), 0, 1);
        add(criteriaTypeComboBox, 1, 1);
        criteriaTypeComboBox.setConverter(CRITERIA_TYPE_TO_STRING);
        criteriaTypeComboBox.setCellFactory(param -> new CriteriaTypeCell());
        criteriaTypeComboBox.setButtonCell(new CriteriaTypeCell()); // TODO: doesn't work wanna have the icon as past of the combobox
        criteriaTypeComboBox.setMaxWidth(Double.MAX_VALUE);
    }

    private static class CriteriaTypeCell extends ListCell<CriteriaType> {
        private static final Map<CriteriaType, String> RESOURCE_MAP = new HashMap<>();
        static {
            RESOURCE_MAP.put(CriteriaType.TAGGING, "tag.png");
            RESOURCE_MAP.put(CriteriaType.OPEN_RATING, "ruler.png");
            RESOURCE_MAP.put(CriteriaType.CONSTRAINED_RATING, "star.png");
        }

        private final ImageView imageView = new ImageView();

        public CriteriaTypeCell() {
            setGraphic(imageView);
            imageView.setCache(true); // TODO: What they are doing
            imageView.setCacheHint(CacheHint.SPEED);

            hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (isSelected() || newValue) {
                    ColorAdjust emptyAdjust = new ColorAdjust();
                    imageView.setEffect(emptyAdjust);
                }
                else {
                    ColorAdjust blueImagePainter = StyleUtil.createColorAdjust(StyleUtil.QUEEN_BLUE);
                    imageView.setEffect(blueImagePainter);
                }
            });
        }

        @Override
        public void updateItem(CriteriaType item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText("");
            }
            else {
                String url = RESOURCE_MAP.get(item);
                imageView.setImage(new Image(url));

                if (isSelected()) {
                    ColorAdjust emptyAdjust = new ColorAdjust();
                    imageView.setEffect(emptyAdjust);
                }
                else {
                    ColorAdjust blueImagePainter = StyleUtil.createColorAdjust(StyleUtil.QUEEN_BLUE);
                    imageView.setEffect(blueImagePainter);
                }

                setText(item.getName());
            }
        }
    }

    private void onShow() {
        disableProperty().bind(selectedCriteria.isNull());
        selectedCriteria.addListener(selectedCriteriaChanged);
        currentTypeViewBuilder.addListener((observable, oldValue, newValue) -> typeViewBuilderChanged(oldValue, newValue)); // TODO:  remove
    }

    private void onHide() {
        disableProperty().unbind();
        selectedCriteria.removeListener(selectedCriteriaChanged);
    }

    private void updateContent(Criteria oldSelection, Criteria newSelection) {
        if (oldSelection != null) {
            oldSelection.propName().unbind();
            oldSelection.propType().unbind();
            oldSelection.propType().addListener(typeChangedListener);
        }

        if (newSelection != null) {
            nameField.setText(newSelection.propName().get());
            criteriaTypeComboBox.setValue(newSelection.propType().get());

            newSelection.propName().bind(nameField.textProperty());
            newSelection.propType().bind(criteriaTypeComboBox.valueProperty());
            newSelection.propType().addListener(typeChangedListener);
            updateTypeContent(newSelection.propType().get());
        }
    }

    private void updateTypeContent(CriteriaType newValue) {
        currentTypeViewBuilder.set(TYPE_CONTENT_MAPPING.get(newValue).apply(selectedCriteria.get()));
    }

    private void typeViewBuilderChanged(TypeViewBuilder oldValue, TypeViewBuilder newValue) {
        if (oldValue != null) {
            oldValue.removeUI(this);
        }
        if (newValue != null) {
            newValue.addUI(this);
        }
    }

    //_______________________________________________ Inner Classes
    interface TypeViewBuilder {
        void addUI(GridPane pane);

        void removeUI(GridPane pane);
    }
    //_______________________________________________ End
}
