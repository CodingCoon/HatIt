package com.hatit.io.example;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.QualitativeSetting;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.data.player.Player;

public class RockNOwl extends ExampleTournament {

    //_______________________________________________ Parameters
    private final Criteria sex = createTaggingCriteria("Geschlecht", "W", "M");
    private final Criteria size = createQuantitative("Größe", "cm");
    private final Criteria throwingSkill = createQualitative("Wurfskill", new QualitativeSetting.Range(1, 5));
    private final Criteria stamina = createQualitative("Ausdauer", new QualitativeSetting.Range(1, 5));

    //_______________________________________________ Initialize
    public RockNOwl() {
        tournament.propName().setValue("Rock'n Owl");
        tournament.nextState();

        tournament.propCriteria().add(sex);
        tournament.propCriteria().add(size);
        tournament.propCriteria().add(throwingSkill);
        tournament.propCriteria().add(stamina);
        tournament.nextState();

        for (int i = 0; i < 100; i++) {
            Player player = createPlayer(i);
            tournament.propPlayers().addAll(player);
        }
        tournament.nextState();

        tournament.getPreferences().propTeamCount().setValue(8);
    }

    //_______________________________________________ Methods


    @Override
    Player createPlayer(int i) {
        Player player = super.createPlayer(i);

        TaggingSetting sexSetting = (TaggingSetting) sex.propSettings().get();
        player.propTaggingStat(sex).setValue(sexSetting.propOptions().get(randomInt(2)).getOption());

        player.propQualitativStat(size).setValue(randomInt(120, 221));
        player.propQualitativStat(throwingSkill).setValue(randomInt(1, 5));
        player.propQualitativStat(stamina).setValue(randomInt(1, 5));

        return player;
    }

    private int randomInt(int maxValue) {
        return randomInt(0, maxValue);
    }

    private Integer randomInt(int minValue, int maxValue) {
        return (int) (Math.random() * (maxValue - minValue)) + minValue;
    }


    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
