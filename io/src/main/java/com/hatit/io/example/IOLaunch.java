package com.hatit.io.example;

import com.hatit.data.tournament.Tournament;
import com.hatit.io.IOService;

import static com.hatit.io.Examples.createRockNOwl;

public class IOLaunch {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    public static void main(String[] args) {
        Tournament rockNOwl = createRockNOwl();
        IOService.createService().store(rockNOwl);

//        Tournament tournament = IOService.createService().loadTournaments("Rock'n Owl");
//        tournament.propName().set("Other Tournament");
//        IOService.createService().store(tournament);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
