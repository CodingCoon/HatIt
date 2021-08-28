package com.hatit.data.criteria;

import javafx.beans.property.*;

import java.util.UUID;

class CriteriaImpl implements Criteria {
    //_______________________________________________ Parameters
    private static int COUNTER = 1;

    private final UUID id;
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<CriteriaType> type = new SimpleObjectProperty<>();
    private final ObjectProperty<Setting> setting = new SimpleObjectProperty<>();

    //_______________________________________________ Initialize
    CriteriaImpl(UUID id) {
        this.id = id;
        this.name.setValue("Kriterium #" + COUNTER++);
        this.type.addListener((observable, oldValue, newValue) -> setting.setValue(newValue.createSetting()));
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
    public <T extends Setting> ReadOnlyObjectProperty<T> propSetting() {
        //noinspection unchecked
        return (ReadOnlyObjectProperty<T>) setting;
    }

    @Override
    public <T extends Setting> T getSetting() {
        //noinspection unchecked
        return (T) setting.get();
    }

    @Override
    public Property<?> getDefaultValueProperty() {
        return setting.get().getDefaultValue();
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
