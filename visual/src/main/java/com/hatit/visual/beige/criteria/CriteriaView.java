package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.visual.ScenePartChangeListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CriteriaView extends VBox {
    //_______________________________________________ Parameters

    private static final Map<CriteriaType, Function<Criteria, Node>> TYPE_CONTENT_MAPPING = new HashMap<>();
    static {
        TYPE_CONTENT_MAPPING.put(null, criteria -> null);
        TYPE_CONTENT_MAPPING.put(CriteriaType.TAGGING     , TaggingTypeContentView::new);
        TYPE_CONTENT_MAPPING.put(CriteriaType.QUALITATIVE , QualitativeTypeContentView::new);
        TYPE_CONTENT_MAPPING.put(CriteriaType.QUANTITATIVE, QuantitativeTypeContentView::new);
    }

    private final ReadOnlyObjectProperty<Criteria> selectedCriteria;

    private final TextField nameField = new TextField("Name");
    private final ComboBox<CriteriaType> criteriaTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(CriteriaType.values()));
    private final StackPane typeContent = new StackPane();

    private final ChangeListener<Criteria> selectedCriteriaChanged = (observable, oldValue, newValue) -> updateContent(oldValue, newValue);
    private final ChangeListener<CriteriaType> typeChangedListener = (observable, oldValue, newValue) -> updateTypeContent(newValue);

    //_______________________________________________ Initialize
    public CriteriaView(ReadOnlyObjectProperty<Criteria> selectedCriteria) {
        this.selectedCriteria = selectedCriteria;

        setSpacing(12);
        getChildren().addAll(new Label("Name:"), nameField,
                             new Label("Typ:"), criteriaTypeComboBox,
                             typeContent);

        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
    }

    //_______________________________________________ Methods
    private void onShow() {
        disableProperty().bind(selectedCriteria.isNull());
        selectedCriteria.addListener(selectedCriteriaChanged);
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
        typeContent.getChildren().clear();

        if (newValue != null) {
            Node typeContentView = TYPE_CONTENT_MAPPING.get(newValue).apply(selectedCriteria.get());
            typeContent.getChildren().add(typeContentView);
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
