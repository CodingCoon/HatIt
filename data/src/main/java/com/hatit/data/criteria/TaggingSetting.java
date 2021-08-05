package com.hatit.data.criteria;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public interface TaggingSetting extends Setting<String> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    ObservableList<Option> propOptions();

    @Override
    default Property<String> getDefaultValue() {
        return new SimpleStringProperty();
    }

    //_______________________________________________ Inner Classes
    class Option {
        private final String option;
        private final String imageResource;

        public Option(String option, String imageResource) {
            this.option = option;
            this.imageResource = imageResource;
        }

        public String getOption() {
            return option;
        }

        public String getImageResource() {
            return imageResource;
        }
    }

    //_______________________________________________ End
}
