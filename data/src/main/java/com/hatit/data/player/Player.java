package com.hatit.data.player;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.ObservableMap;

import java.util.UUID;

public interface Player {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Player of() {
        return new PlayerImpl();
    }

    //_______________________________________________ Methods
    UUID getID();

    StringProperty propName();

    String getName();

    // TODO: ObservableNumberValue
    ObservableMap<UUID, ObjectProperty<?>> propStats();

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
