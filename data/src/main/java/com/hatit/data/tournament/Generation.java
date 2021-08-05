package com.hatit.data.tournament;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.criteria.QualitativeSetting;
import com.hatit.data.criteria.TaggingSetting;
import com.hatit.data.generation.Preferences;
import com.hatit.data.player.Player;
import com.hatit.data.team.Team;
import com.hatit.data.team.ValuedPlayer;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

import java.util.*;
import java.util.stream.Collectors;

class Generation {
    //_______________________________________________ Parameters
    private final Tournament tournament;

    private final List<Criteria> taggingCriteria = new ArrayList<>();
    private final Map<Criteria, ValueRange> valueRanges = new HashMap<>();

    private final List<ValuedPlayer> playerValues = new ArrayList<>();

    private final List<Group> groups = new ArrayList<>();

    private final List<TempTeam> teams = new ArrayList<>();

    //_______________________________________________ Initialize
    Generation(Tournament tournament) {
        this.tournament = tournament;
    }

    //_______________________________________________ Methods
    List<Team> start() {
        setupValueRanges();
        setupTaggingGroups();
        determinePlayerValues();
        splitUpPlayers();
        generateTeams();
        draftPlayers();
        return createTeams();
    }

    private List<Team> createTeams() {
        return teams.stream().map(tempTeam -> new Team(tempTeam.name, tempTeam.playerList)).collect(Collectors.toList());
    }

    private void setupValueRanges() {
        for (Preferences.CriteriaUsage criteriaUsage : tournament.getPreferences().getUsages()) {
            if (criteriaUsage.isActive()) {
                Criteria criteria = criteriaUsage.getCriteria();
                CriteriaType criteriaType = criteriaUsage.getCriteriaType();

                if (criteriaType == CriteriaType.TAGGING) {
                    taggingCriteria.add(criteria);
                }
                else if (criteriaType == CriteriaType.QUALITATIVE) {
                    QualitativeSetting setting = (QualitativeSetting) criteria.propSettings().get();
                    QualitativeSetting.Range range = setting.propRange().get();
                    valueRanges.put(criteria, new ValueRange(range.getMin(), range.getMax(), criteriaUsage.propFactor().getValue()));
                }
                else if (criteriaType == CriteriaType.QUANTITATIVE) {
                    double minValue = Double.MAX_VALUE;
                    double maxValue = Double.MIN_VALUE;

                    for (Player player : tournament.propPlayers()) {
                        Property<Number> statValue = player.propQualitativStat(criteria);
                        double value = statValue.getValue().doubleValue();
                        if (value < minValue) {
                            minValue = value;
                        }
                        if (value > maxValue) {
                            maxValue = value;
                        }
                    }

                    valueRanges.put(criteria, new ValueRange(minValue, maxValue, criteriaUsage.propFactor().getValue()));
                }
                else {
                    throw new UnsupportedOperationException("snh");
                }
            }
        }
    }

    private void setupTaggingGroups() {
        List<Criteria> taggingCriteria = new ArrayList<>();
        for (Preferences.CriteriaUsage usage : tournament.getPreferences().getUsages()) {
            if (usage.isActive() && usage.getCriteriaType() == CriteriaType.TAGGING) {
                taggingCriteria.add(usage.getCriteria());
            }
        }
        createPermutation(new ArrayList<>(), taggingCriteria, groups, 0);
    }

    private void createPermutation(List<String> collectedTags, List<Criteria> taggingCriteria, List<Group> permutations, int currentLevel) {
        if (taggingCriteria.size() == currentLevel) {
            // no more permutations to add
            permutations.add(new Group(collectedTags));
            return;
        }

        Criteria criteria = taggingCriteria.get(currentLevel);
        List<String> tags = ((TaggingSetting) criteria.propSettings().get()).propOptions().stream().map(TaggingSetting.Option::getOption).collect(Collectors.toList());
        for (String tag : tags) {
            List<String> newTagBranch = new ArrayList<>(collectedTags);
            newTagBranch.add(tag);
            createPermutation(new ArrayList<>(newTagBranch), taggingCriteria, permutations, currentLevel + 1);
        }
    }

    private void determinePlayerValues() {
        for (Player player : tournament.propPlayers()) {
            double playerValue = 0;

            for (Map.Entry<Criteria, ValueRange> entry : valueRanges.entrySet()) {
                Criteria key = entry.getKey();
                CriteriaType criteriaType = key.propType().get();
                double value;

                if (criteriaType == CriteriaType.QUALITATIVE) {
                    Property<Number> valueProperty = player.propQualitativStat(key);
                    value = valueProperty.getValue().intValue(); // TODO:
                }
                else if (criteriaType == CriteriaType.QUANTITATIVE) {
                    Property<Number> valueProperty = player.propQualitativStat(key);
                    value = valueProperty.getValue().doubleValue();
                }
                else {
                    throw new IllegalStateException("snh");
                }

                playerValue += entry.getValue().getValue(value);
            }

            playerValues.add(new ValuedPlayer(player, playerValue, ""));
        }
        Collections.sort(playerValues);
    }

    private void splitUpPlayers() {
        Map<String, Group> groupsMapping = new HashMap<>();
        groups.forEach(group -> groupsMapping.put(group.permutationCode, group));

        while (! playerValues.isEmpty()) {
            ValuedPlayer remove = playerValues.remove(0);
            Player player = remove.getPlayer();

            List<String> tagCode = new ArrayList<>();
            for (Criteria tagCriteria : taggingCriteria) {
                ObservableValue<String> tagProperty = player.propTaggingStat(tagCriteria);
                tagCode.add(tagProperty.getValue());
            }

            groupsMapping.get(Group.buildPermutationCode(tagCode)).addPlayerValue(remove);
        }
    }

    private void generateTeams() {
        for (int i = 0; i < tournament.getPreferences().propTeamCount().get(); i++) {
            teams.add(new TempTeam("T" + i));
        }
    }

    private void draftPlayers() {
        int teamCount = teams.size();
        List<Group> waitingQueue = new ArrayList<>(); // in der Warteschlange, wenn nicht mehr genug Spieler für alle Teams

        int round = 1;

        while (! groups.isEmpty()) {
            Group group = groups.remove(0);

            while (group.playerValueList.size() >= teamCount) {
                Collections.sort(teams);

                splitUpPlayers(group, teamCount);
                printRound(round);
                round++;

            }
            if (! group.playerValueList.isEmpty()) {
                waitingQueue.add(group);
            }
        }

        while (! waitingQueue.isEmpty()) {
            Collections.sort(teams);
            Group group = waitingQueue.remove(0);

            int playerValueSize = group.playerValueList.size();
            splitUpPlayers(group, playerValueSize);
            printRound(round);
            round++;
        }
    }

    private void splitUpPlayers(Group group, int playerValueSize) {
        for (int i = 0; i < playerValueSize; i++) {
            ValuedPlayer remove = group.playerValueList.remove(0);
            teams.get(i).addPlayer(remove);
        }
    }

    private void printRound(int round) {
        System.out.println("--- Runde: " + round);
        teams.forEach(System.out::println);
    }

    //_______________________________________________ Inner Classes
    public static final class Group {
        private final String permutationCode;
        private final List<ValuedPlayer> playerValueList = new ArrayList<>();

        public Group(List<String> collectedTags) {
            this.permutationCode = buildPermutationCode(collectedTags);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Group group = (Group) o;
            return permutationCode.equals(group.permutationCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(permutationCode);
        }

        @Override
        public String toString() {
            return permutationCode;
        }

        private void addPlayerValue(ValuedPlayer playerValue) {
            playerValueList.add(playerValue);
        }

        private static String buildPermutationCode(List<String> collectedTags) {
            StringBuilder builder = new StringBuilder();
            for (String tag : collectedTags) {
                if (!builder.isEmpty()) {
                    builder.append("/");
                }
                builder.append(tag);
            }

            return builder.toString();
        }
    }

    private static final class ValueRange {
        private final double minValue;
        private final double maxValue;
        private final double factor;

        public ValueRange(double minValue, double maxValue, double factor) {
            assert maxValue > minValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.factor = factor;
        }

        @Override
        public String toString() {
            return "(Min: " + minValue + "; Max: " + maxValue + " [x " + factor + "])";
        }

        double getValue(double playerValue) {
            if (maxValue == minValue) {
                assert maxValue == playerValue;
                return 100d * factor;
            }
            else {
                double bruttoValue =  100d * (playerValue - minValue) / (maxValue - minValue);
                //noinspection UnnecessaryLocalVariable
                double nettoValue = factor * bruttoValue;
                return nettoValue;
            }
        }
    }

    private static final class TempTeam implements Comparable<TempTeam> {
        private final String name;
        private final List<ValuedPlayer> playerList = new ArrayList<>();
        private double teamValue = 0;

        public TempTeam(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Team " + name + " ("  + playerList.size() + " - Value: " + teamValue + ")";
        }

        private void addPlayer(ValuedPlayer playerValue) {
            playerList.add(playerValue);
            teamValue += playerValue.getValue();
        }

        @Override
        public int compareTo(TempTeam o) {
            int playerComparing = Integer.compare(playerList.size(), o.playerList.size());
            if (playerComparing == 0) {
                return Double.compare(teamValue, o.teamValue);
            }
            else {
                return playerComparing;
            }
        }
    }

    //_______________________________________________ End
}
