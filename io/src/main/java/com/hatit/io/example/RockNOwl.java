package com.hatit.io.example;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.QualitativeSetting;
import com.hatit.data.criteria.Setting;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.data.player.Player;
import com.hatit.data.tournament.Tournament;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

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
        tournament.nextState();
    }

    //_______________________________________________ Methods


    @Override
    Player createPlayer(int i) {
        Player player = super.createPlayer(i);

        TaggingSetting sexSetting = (TaggingSetting) sex.propSettings().get();
        player.propStats().put(sex.getID(), new SimpleObjectProperty<>(sexSetting.propOptions().get(randomInt(2)).getOption()));

        player.propStats().put(size.getID(), new SimpleObjectProperty<>("" + randomInt(120, 221)));
        player.propStats().put(throwingSkill.getID(), new SimpleObjectProperty<>(randomInt(1, 5)));
        player.propStats().put(stamina.getID(), new SimpleObjectProperty<>(randomInt(1, 5)));

        return player;
    }

    private int randomInt(int maxValue) {
        return randomInt(0, maxValue);
    }

    private int randomInt(int minValue, int maxValue) {
        return (int) (Math.random() * (maxValue - minValue)) + minValue;
    }


    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
