package com.hatit.data.player;

import com.hatit.data.criteria.Criteria;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public interface Player {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Player of() {
        return new PlayerImpl(UUID.randomUUID());
    }

    static Player of(UUID id) {
        return new PlayerImpl(id);
    }

    //_______________________________________________ Methods
    UUID getID();

    StringProperty propName();

    String getName();

    <T extends Property<Number>> T propQualitativStat(Criteria criteria);

    StringProperty propTaggingStat(Criteria criteria);

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
