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
        private final StringProperty option = new SimpleStringProperty("");

        public Option(String option) {
            this.option.set(option);
        }

        public StringProperty propOption() {
            return option;
        }

        public String getOption() {
            return option.get();
        }
    }

    //_______________________________________________ End
}
