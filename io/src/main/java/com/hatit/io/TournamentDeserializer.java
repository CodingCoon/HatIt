package com.hatit.io;

import com.google.gson.*;
import com.hatit.data.criteria.*;
import com.hatit.data.generation.Preferences;
import com.hatit.data.player.Player;
import com.hatit.data.tournament.Tournament;
import com.hatit.data.tournament.TournamentState;

import java.lang.reflect.Type;
import java.util.UUID;

public class TournamentDeserializer implements JsonDeserializer<Tournament> {

    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public Tournament deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject tournamentJson = json.getAsJsonObject();

        String id    = tournamentJson.get("id").getAsString();
        String name  = tournamentJson.get("name").getAsString();
        String state = tournamentJson.get("state").getAsString();
        Tournament tournament = Tournament.of(UUID.fromString(id), name, TournamentState.valueOf(state));

        loadCriteria(tournament, tournamentJson);
        loadPlayer(tournament, tournamentJson);
        loadPreferences(tournament, tournamentJson);
        return tournament;
    }

    private void loadCriteria(Tournament tournament, JsonObject json) {
        JsonArray criteriaJsons = json.getAsJsonArray("criteria");
        for (JsonElement element : criteriaJsons) {
            JsonObject criteriaObject = element.getAsJsonObject();

            String idString = criteriaObject.get("id").getAsString();
            UUID id = UUID.fromString(idString);
            Criteria criteria = Criteria.of(id);

            String name = criteriaObject.get("name").getAsString();
            criteria.propName().set(name);

            String typeString = criteriaObject.get("type").getAsString();
            CriteriaType type = CriteriaType.valueOf(typeString);
            criteria.propType().set(type);

            if (criteriaObject.has("open setting")) {
                String unit = criteriaObject.get("open setting").getAsString();
                OpenRatingSetting setting = criteria.getSetting();
                setting.setUnit(unit);
            }
            else if (criteriaObject.has("constrained setting")) {
                JsonObject constrainedRatingJson = criteriaObject.get("constrained setting").getAsJsonObject();
                ConstrainedRatingSetting setting = criteria.getSetting();
                int min = constrainedRatingJson.get("min").getAsInt();
                int max = constrainedRatingJson.get("max").getAsInt();
                setting.setRange(new ConstrainedRatingSetting.Range(min, max));
            }
            else if (criteriaObject.has("tagging setting")) {
                JsonArray taggingSettingJson = criteriaObject.get("tagging setting").getAsJsonArray();
                TaggingSetting setting = criteria.getSetting();
                for(JsonElement option : taggingSettingJson) {
                    setting.propOptions().add(new TaggingSetting.Option(option.getAsString()));
                }
            }

            tournament.propCriteria().add(criteria);
        }
    }

    private void loadPlayer(Tournament tournament, JsonObject json) {
        JsonArray playerJsons = json.getAsJsonArray("player");
        for (JsonElement element : playerJsons) {
            JsonObject playerObject = element.getAsJsonObject();

            String id   = playerObject.get("id").getAsString();
            Player player = Player.of(UUID.fromString(id));

            String name = playerObject.get("name").getAsString();
            player.propName().set(name);

            JsonElement stats = playerObject.get("stats");
            for (Criteria criteria : tournament.propCriteria()) {
                JsonElement value = stats.getAsJsonObject().get(criteria.getID().toString());

                switch (criteria.getType()) {
                    case TAGGING -> player.propTaggingStat(criteria).set(value.getAsString());
                    case CONSTRAINED_RATING -> player.propQualitativStat(criteria).setValue(value.getAsInt());
                    case OPEN_RATING -> player.propQualitativStat(criteria).setValue(value.getAsDouble());
                    default -> throw new IllegalArgumentException("snh: " + criteria.getType());
                }
            }

            tournament.propPlayers().add(player);
        }
    }

    private void loadPreferences(Tournament tournament, JsonObject json) {
        JsonObject preferenceJson = json.getAsJsonObject("preferences");
        Preferences preferences = tournament.getPreferences();

        int teamCount = preferenceJson.get("teamCount").getAsInt();
        preferences.propTeamCount().setValue(teamCount);

        JsonArray usages = preferenceJson.getAsJsonArray("usages");
        for (JsonElement element : usages) {
            JsonObject usageObject = element.getAsJsonObject();
            String criteriaID = usageObject.get("criteria").getAsString();

            for (Preferences.CriteriaUsage criteriaUsage : preferences.getUsages()) {
                if (criteriaUsage.getCriteria().getID().toString().equals(criteriaID)) {
                    boolean active = usageObject.get("active").getAsBoolean();
                    double factor = usageObject.get("factor").getAsDouble();

                    criteriaUsage.propActive().set(active);
                    criteriaUsage.propFactor().set(factor);
                }
            }
        }
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
