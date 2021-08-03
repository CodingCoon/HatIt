package com.hatit.data.tournament;

public enum TournamentState {
    //_______________________________________________ Parameters
    TOURNAMENT("Turnierdaten") {
        @Override
        public boolean hasPrevious() {
            return false;
        }
    },
    CRITERIA("Kriterien"),
    PLAYER("Spieler"),
    PREFERENCES("Gewichtung"),
    TEAM("Teams") {
        @Override
        public boolean hasNext() {
            return false;
        }
    };

    private final String label;

    //_______________________________________________ Initialize
    TournamentState(String label) {
        this.label = label;
    }

    //_______________________________________________ Methods
    public String getLabel() {
        return label;
    }

    public boolean hasNext() {
        return true;
    }

    public boolean hasPrevious() {
        return true;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End

}
