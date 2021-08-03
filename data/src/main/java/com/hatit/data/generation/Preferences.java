package com.hatit.data.generation;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.tournament.Tournament;
import javafx.beans.property.*;

public interface Preferences {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    static Preferences of(Tournament tournament) {
        return new PreferencesImpl(tournament);
    }
    //_______________________________________________ Methods
    IntegerProperty propTeamCount();

    Iterable<CriteriaUsage> getUsages();

    //_______________________________________________ Inner Classes
    class CriteriaUsage {
        private final Criteria criteria;
        private final BooleanProperty active = new SimpleBooleanProperty(true);
        private final DoubleProperty factor = new SimpleDoubleProperty(1);

        CriteriaUsage(Criteria criteria) {
            this.criteria = criteria;
        }

        public Criteria getCriteria() {
            return criteria;
        }

        public String getCriteriaName() {
            return criteria.propName().get();
        }

        public CriteriaType getCriteriaType() {
            return criteria.propType().get();
        }

        public boolean isActive() {
            return active.get();
        }

        public BooleanProperty propActive() {
            return active;
        }

        public DoubleProperty propFactor() {
            return factor;
        }
    }

    //_______________________________________________ End
}
