package com.hatit.data.criteria;

import javafx.beans.property.*;

import java.util.UUID;

class CriteriaImpl implements Criteria {
    //_______________________________________________ Parameters
    private static int COUNTER = 1;

    private final UUID id;
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<CriteriaType> type = new SimpleObjectProperty<>();
    private final ObjectProperty<Setting> settings = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    CriteriaImpl() {
        this.id = UUID.randomUUID();
        this.name.setValue("Kriterium #" + COUNTER++);

        type.addListener((observable, oldValue, newValue) -> settings.setValue(newValue.createSetting()));
    }

    //_______________________________________________ Methods
    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public StringProperty propName() {
        return name;
    }

    @Override
    public ObjectProperty<CriteriaType> propType() {
        return type;
    }

    @Override
    public CriteriaType getType() {
        return type.get();
    }

    @Override
    public <T extends Setting> ReadOnlyObjectProperty<T> propSettings() {
        //noinspection unchecked
        return (ReadOnlyObjectProperty<T>) settings;
    }

    @Override
    public Property<?> getDefaultValueProperty() {
        return settings.get().getDefaultValue();
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
