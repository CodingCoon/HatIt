package com.hatit.io;

import com.hatit.data.tournament.Tournament;

import java.util.List;

public interface IOService {

    //_______________________________________________ Parameters
    String FILE_RESOURCE = "hatit/";
    String EXTENSION = ".json";

    //_______________________________________________ Initialize
    static IOService createService() { // TODO: load by Service loading
        return new GsonService();
    }

    //_______________________________________________ Methods
    List<Tournament> loadTournaments();

    void store(Tournament tournament);

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
