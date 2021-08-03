package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.QuantitativeSetting;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class QuantitativeTypeContentView extends VBox {
    //_______________________________________________ Parameters
    private final QuantitativeSetting setting;

    private final TextField unitField = new TextField();

    //_______________________________________________ Initialize
    public QuantitativeTypeContentView(Criteria criteria) {
        this.setting = (QuantitativeSetting) criteria.propSettings().get();
        initUI();

        unitField.setText(setting.propUnit().get());
        unitField.textProperty().addListener((observable, oldValue, newValue) -> setting.propUnit().setValue(newValue));
    }

    //_______________________________________________ Methods
    private void initUI() {
        setSpacing(12);
        getChildren().addAll(
                new Label("Einheit:"),
                unitField);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
