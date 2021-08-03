package com.hatit.data.player;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.UUID;

class PlayerImpl implements Player {

    //_______________________________________________ Parameters
    private final UUID id;
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableMap<UUID, ObjectProperty<?>> stats = FXCollections.observableHashMap();

    //_______________________________________________ Initialize
    PlayerImpl() {
        this.id = UUID.randomUUID();
    }

    //_______________________________________________ Methods
    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public StringProperty propName() {
        return name;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public ObservableMap<UUID, ObjectProperty<?>> propStats() {
        return stats;
    }
    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
