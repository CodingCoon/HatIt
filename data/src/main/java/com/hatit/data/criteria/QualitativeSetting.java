package com.hatit.data.criteria;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

public interface QualitativeSetting extends Setting<Number> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    ObjectProperty<Range> propRange();

    Range getRange();

    @Override
    default Property<Number> getDefaultValue() {
        return new SimpleIntegerProperty(propRange().getValue().getMin());
    }

    //_______________________________________________ Inner Classes
    class Range {   // TODO: record
        private final int min;
        private final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }

    //_______________________________________________ End
}
