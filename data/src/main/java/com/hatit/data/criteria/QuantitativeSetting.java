package com.hatit.data.criteria;

import javafx.beans.property.StringProperty;

public interface QuantitativeSetting extends Setting<String> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    StringProperty propUnit();

    @Override
    default String getDefaultValue() {
        return "0";
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
