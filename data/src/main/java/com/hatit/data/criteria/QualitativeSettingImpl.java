package com.hatit.data.criteria;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class QualitativeSettingImpl implements QualitativeSetting {
    //_______________________________________________ Parameters
    private final ObjectProperty<Range> range = new SimpleObjectProperty<>(new Range(1, 5));

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public ObjectProperty<Range> propRange() {
        return range;
    }

    @Override
    public Range getRange() {
        return range.get();
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
