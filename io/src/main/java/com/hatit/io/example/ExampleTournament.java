package com.hatit.io.example;

import com.hatit.data.criteria.*;
import com.hatit.data.player.Player;
import com.hatit.data.tournament.Tournament;

import java.util.Arrays;
import java.util.List;

class ExampleTournament {
    //_______________________________________________ Parameters
    private static final List<String> NAME_LIST = Arrays.asList(
            "Lucy", "Lukas", "Ella", "Konstantin", "Amy", "Ben", "Emely", "Jonas", "Finja", "Elias", "Amelie", "Niklas", "Luise", "David", "Frieda", "Oskar", "Katharina", "Philipp", "Romy", "Leon", "Juna", "Noah",
            "Theresa", "Luis", "Eva", "Paul", "Julia", "Finn", "Anna", "Felix", "Carla", "Julian", "Paulina", "Maximilian", "Elisabeth", "Henry", "Rosa", "Tim", "Mia", "Karl", "Maya", "Friedrich", "Selma", "Peter",
            "Edda", "Quirin", "Flora", "Liam", "Berenike", "Linus", "Simone", "Quentin", "Elena", "Paul", "Meike", "Johannes", "Susanne", "Alexander", "Annika", "Anton",
            "Augusta", "Aras", "Alba", "Asis", "Wilma", "Adrian", "Annegret", "Arthur", "Aglaia", "Adam", "Aaliyah", "Arian", "Annabelle", "Amos", "Alma", "Arik", "Alicia", "Ake", "Anette", "Altfried",
            "Astrid", "Ari", "Anisha", "Andreas", "Antke", "Allessandro", "Abigail", "Achim", "Aideen", "Ben", "Aini", "Bela", "Aida", "Baldur", "Aamenah", "Benedikt", "Ariane", "Beat", "Adriana", "Bernd", "Alexandra", "Bertram",
            "Ava", "Blue", "Arielle", "Badi", "Allissa", "Batiste", "Aamu", "Bastian", "Arzu", "Caleb", "Anouk", "Caspar", "Andrea", "Calvin", "Bianca", "Cadmus", "Blanka", "Christoph", "Benita", "Cedrik", "Bettina", "Camern",
            "Bamika", "Carsten", "Bente", "Cainan", "Barbara", "Cem", "Berit", "Carl", "Bentje", "Cyranus", "Birte", "Curt", "Brigitte", "Daniel", "Christiane", "Dominik",
            "Charlotte", "Darius", "Catherina", "Dario", "Caroline", "Dag", "Caren", "Diminic", "Caecilia", "Damian", "Celine", "Diego", "Coco", "Dieter", "Chaya", "Demian", "Dalia", "Dewis", "Deenah", "Dirk",
            "Daphne", "Donald", "Delia", "Enzo", "Dari", "Emil", "Doerte", "Erik", "Djamila", "Edwin", "Dominique", "Eliah", "Doerte", "Ethan", "Dorothee", "Erwin", "Emira", "Eliot",
            "Emily", "Enes", "Elif", "Emilio", "Ellen", "Ebbo", "Enna", "Eberhard", "Ebba", "Edgar", "Eleni", "Fabrizius", "Freya", "Finn", "Fiona", "Fabian", "Franziska", "Fabio", "Luzia", "Finjas", "Fabienne", "Franz");

    final Tournament tournament;

    //_______________________________________________ Initialize
    protected ExampleTournament() {
        this.tournament = Tournament.of();
    }

    //_______________________________________________ Methods


    public Tournament getTournament() {
        return tournament;
    }

    Player createPlayer(int i) {
        Player player = Player.of();
        player.propName().setValue(NAME_LIST.get(i % NAME_LIST.size()));
        return player;
    }

    Criteria createTaggingCriteria(String name, String... options) {
        Criteria criteria = Criteria.of();
        criteria.propName().setValue(name);
        criteria.propType().set(CriteriaType.TAGGING);
        TaggingSetting setting = (TaggingSetting) criteria.propSettings().get();
        for (String option : options) {
            setting.propOptions().add(new TaggingSetting.Option(option, ""));
        }
        return criteria;
    }

    Criteria createQuantitative(String name, String unit) {
        Criteria criteria = Criteria.of();
        criteria.propName().set(name);
        criteria.propType().set(CriteriaType.QUANTITATIVE);
        QuantitativeSetting setting = (QuantitativeSetting) criteria.propSettings().get();
        setting.propUnit().setValue(unit);
        return criteria;
    }

    Criteria createQualitative(String name, QualitativeSetting.Range range) {
        Criteria criteria = Criteria.of();
        criteria.propName().set(name);
        criteria.propType().set(CriteriaType.QUALITATIVE);
        QualitativeSetting setting = (QualitativeSetting) criteria.propSettings().get();
        setting.propRange().setValue(range);
        return criteria;
    }
    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
