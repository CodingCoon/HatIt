package com.hatit.data.criteria;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public interface Criteria {

    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Criteria of() {
        return new CriteriaImpl();
    }

    //_______________________________________________ Methods
    UUID getID();

    StringProperty propName();

    ObjectProperty<CriteriaType> propType();

    <T extends Setting> ReadOnlyObjectProperty<T> propSettings();

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
