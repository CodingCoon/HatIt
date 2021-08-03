package com.hatit.data.criteria;

import javafx.beans.property.ObjectProperty;

public interface QualitativeSetting extends Setting {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    ObjectProperty<Range> propRange();

    @Override
    default Integer getDefaultValue() {
        return propRange().getValue().getMin();
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
