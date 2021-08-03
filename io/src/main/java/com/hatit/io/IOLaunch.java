package com.hatit.io;

import com.hatit.data.tournament.Tournament;

import static com.hatit.io.Examples.createRockNOwl;

public class IOLaunch {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    public static void main(String[] args) {
        Tournament rockNOwl = createRockNOwl();
        IOService.createService().store(rockNOwl);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
