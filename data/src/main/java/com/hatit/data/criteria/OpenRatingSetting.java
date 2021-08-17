package com.hatit.data.criteria;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

public interface OpenRatingSetting extends Setting {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    void setUnit(String unit);

    String getUnit();

    @Override
    default DoubleProperty getDefaultValue() {
        return new SimpleDoubleProperty(0);
    }


    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
