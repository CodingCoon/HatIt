package com.hatit.data.team;

import com.hatit.data.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    //_______________________________________________ Parameters
    private final String name;
    private final List<ValuedPlayer> playerList;
    private final double teamValue;

    //_______________________________________________ Initialize
    public Team(String name, List<ValuedPlayer> playerList) {
        this.name = name;
        this.playerList = new ArrayList<>(playerList);
        this.teamValue = playerList.stream().mapToDouble(ValuedPlayer::getValue).sum();
    }

    //_______________________________________________ Methods

    public String getName() {
        return name;
    }

    public List<ValuedPlayer> getPlayerList() {
        return playerList;
    }

    public double getTeamValue() {
        return teamValue;
    }

    public int getFormattedTeamValue() {
        return (int) teamValue;
    }

//_______________________________________________ Inner Classes
    //_______________________________________________ End
}
