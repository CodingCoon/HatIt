package com.hatit.data.team;

import com.hatit.data.player.Player;

public class ValuedPlayer implements Comparable<ValuedPlayer> {
    //_______________________________________________ Parameters
    private final Player player;
    private final double value;
    private final String tag;

    //_______________________________________________ Initialize
    public ValuedPlayer(Player player, double value, String tag) {
        this.player = player;
        this.value = value;
        this.tag = tag;
    }

    //_______________________________________________ Methods
    @Override
    public String toString() {
        return player.propName().get() + " (" + value + ")";
    }

    @Override
    public int compareTo(ValuedPlayer o) {
        return Double.compare(o.value, value);
    }

    public Player getPlayer() {
        return player;
    }

    public double getValue() {
        return value;
    }

    public int getFormattedValue() {
        return (int) value;
    }

    public String getTag() {
        return tag;
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
