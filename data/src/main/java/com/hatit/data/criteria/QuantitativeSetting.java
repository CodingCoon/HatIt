package com.hatit.data.criteria;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

public interface QuantitativeSetting extends Setting<Number> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    StringProperty propUnit();

    String getUnit();

    @Override
    default Property<Number> getDefaultValue() {
        return new SimpleDoubleProperty(0);
    }


    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
