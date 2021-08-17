package com.hatit.data.criteria;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface TaggingSetting extends Setting {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    ObservableList<Option> propOptions();

    @Override
    default StringProperty getDefaultValue() {
        return new SimpleStringProperty();
    }

    //_______________________________________________ Inner Classes
    class Option {
        private final String option;

        public Option(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }
    }

    //_______________________________________________ End
}
