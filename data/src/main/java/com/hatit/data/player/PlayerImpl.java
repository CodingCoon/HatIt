package com.hatit.data.player;

import com.hatit.data.criteria.Criteria;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

class PlayerImpl implements Player {

    //_______________________________________________ Parameters
    private final UUID id;
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableMap<UUID, Property<?>> stats = FXCollections.observableHashMap();

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
    public <T extends Property<Number>> T propQualitativStat(Criteria criteria) {
        //noinspection unchecked
        return (T) stats.computeIfAbsent(criteria.getID(), uuid -> criteria.getDefaultValueProperty());
    }

    @Override
    public StringProperty propTaggingStat(Criteria criteria) {
        //noinspection ConstantConditions
        return (StringProperty) stats.computeIfAbsent(criteria.getID(), uuid -> criteria.getDefaultValueProperty());
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
