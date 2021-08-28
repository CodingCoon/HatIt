package com.hatit.data.generation;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.tournament.Tournament;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PreferencesImpl implements Preferences {
    //_______________________________________________ Parameters
    private final IntegerProperty teamCount = new SimpleIntegerProperty(2);

    private final Map<Criteria, CriteriaUsage> usages = new HashMap<>();

    //_______________________________________________ Initialize
    PreferencesImpl(Tournament tournament) {
        tournament.propCriteria().addListener(this::updateUsages);
    }

    //_______________________________________________ Methods
    public IntegerProperty propTeamCount() {
        return teamCount;
    }

    @Override
    public Iterable<CriteriaUsage> getUsages() {
        return usages.values();
    }

    private void updateUsages(ListChangeListener.Change<? extends Criteria> c) {
        while (c.next()) {
            if (c.wasAdded()) {
                for (Criteria criteria : c.getAddedSubList()) {
                    usages.put(criteria, new CriteriaUsage(criteria));
                }
            }
            else if (c.wasRemoved()) {
                for (Criteria criteria : c.getRemoved()) {
                    usages.remove(criteria);
                }
            }
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}