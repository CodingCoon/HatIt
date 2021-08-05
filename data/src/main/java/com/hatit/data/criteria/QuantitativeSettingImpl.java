package com.hatit.data.criteria;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class QuantitativeSettingImpl implements QuantitativeSetting {
    //_______________________________________________ Parameters
    private final StringProperty unit = new SimpleStringProperty();

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public StringProperty propUnit() {
        return unit;
    }

    @Override
    public String getUnit() {
        return unit.get();
    }
    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
