package com.hatit.data.criteria;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public interface Criteria {

    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Criteria of() {
        return new CriteriaImpl(UUID.randomUUID());
    }

    static Criteria of(UUID id) {
        return new CriteriaImpl(id);
    }

    //_______________________________________________ Methods
    UUID getID();

    StringProperty propName();

    default String getName() {
        return propName().get();
    }

    ObjectProperty<CriteriaType> propType();

    default CriteriaType getType() {
        return propType().get();
    }

    <T extends Setting> ReadOnlyObjectProperty<T> propSetting();

    <T extends Setting> T getSetting();

    Property<?> getDefaultValueProperty();

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
