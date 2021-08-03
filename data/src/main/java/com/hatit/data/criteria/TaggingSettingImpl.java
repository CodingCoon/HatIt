package com.hatit.data.criteria;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class TaggingSettingImpl implements TaggingSetting {
    //_______________________________________________ Parameters
    private final ObservableList<Option> options = FXCollections.observableArrayList();

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public ObservableList<Option> propOptions() {
        return options;
    }
    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
