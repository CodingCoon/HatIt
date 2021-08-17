package com.hatit.data.criteria;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public interface ConstrainedRatingSetting extends Setting {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    void setRange(Range range);

    Range getRange();

    @Override
    default IntegerProperty getDefaultValue() {
        return new SimpleIntegerProperty(getRange().getMin());
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
