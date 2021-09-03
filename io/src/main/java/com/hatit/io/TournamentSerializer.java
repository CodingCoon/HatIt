package com.hatit.io;

import com.google.gson.*;
import com.hatit.data.criteria.*;
import com.hatit.data.generation.Preferences;
import com.hatit.data.player.Player;
import com.hatit.data.tournament.Tournament;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.List;

class TournamentSerializer implements JsonSerializer<Tournament> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public JsonElement serialize(Tournament src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject tournamentJson = new JsonObject();
        tournamentJson.addProperty("id", src.getId().toString());
        tournamentJson.addProperty("name", src.propName().get());
        tournamentJson.addProperty("state", src.getState().get().name());
        tournamentJson.addProperty("version", IOService.VERSION);
        tournamentJson.addProperty("last change", DateTime.now().toString());

        JsonArray criteriaArray = src.propCriteria().stream().map(this::createCriteriaJson).collect(JsonArray::new, JsonArray::add, (jsonElements, jsonElements2) -> {});
        tournamentJson.add("criteria", criteriaArray);

        JsonArray playerArray = src.propPlayers().stream().map(player -> createPlayerJson(player, src.propCriteria())).collect(JsonArray::new, JsonArray::add, (jsonElements, jsonElements2) -> {});
        tournamentJson.add("player", playerArray);

        JsonElement preferenceJson = createPreferenceJson(src.getPreferences());
        tournamentJson.add("preferences", preferenceJson);

        return tournamentJson;
    }

    private JsonElement createCriteriaJson(Criteria criteria) {
        JsonObject criteriaJson = new JsonObject();
        criteriaJson.addProperty("id", criteria.getID().toString());
        criteriaJson.addProperty("name", criteria.getName());
        criteriaJson.addProperty("type", criteria.getType().name());

        switch (criteria.getType()) {
            case TAGGING -> addTaggingSetting(criteria, criteriaJson);
            case CONSTRAINED_RATING -> addConstrainedSetting(criteria, criteriaJson);
            case OPEN_RATING -> addOpenSetting(criteria, criteriaJson);
            // default -> if not chosen, than store nothing
        }

        return criteriaJson;
    }

    private void addOpenSetting(Criteria criteria, JsonObject criteriaJson) {
        OpenRatingSetting openRatingSetting = criteria.getSetting();
        criteriaJson.add("open setting", new JsonPrimitive(openRatingSetting.getUnit()));
    }

    private void addConstrainedSetting(Criteria criteria, JsonObject criteriaJson) {
        ConstrainedRatingSetting constrainedRatingSetting = criteria.getSetting();
        ConstrainedRatingSetting.Range range = constrainedRatingSetting.getRange();
        JsonObject rangeJson = new JsonObject();
        rangeJson.addProperty("min", range.getMin());
        rangeJson.addProperty("max", range.getMax());
        criteriaJson.add("constrained setting", rangeJson);
    }

    private void addTaggingSetting(Criteria criteria, JsonObject criteriaJson) {
        TaggingSetting taggingSetting = criteria.getSetting();
        JsonArray optionsJson = new JsonArray();
        taggingSetting.propOptions().forEach(o -> optionsJson.add(o.getOption()));
        criteriaJson.add("tagging setting", optionsJson);
    }

    private JsonElement createPlayerJson(Player player, List<Criteria> criteriaList) {
        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("id", player.getID().toString());
        playerJson.addProperty("name", player.getName());

        JsonObject criteriaJson = new JsonObject();
        for (Criteria criteria : criteriaList) {

            if (criteria.getType() == CriteriaType.TAGGING) {
                String tag = player.propTaggingStat(criteria).get();
                criteriaJson.addProperty(criteria.getID().toString(), tag);
            }
            else {
                Number value = player.propQualitativStat(criteria).getValue();
                criteriaJson.addProperty(criteria.getID().toString(), value);
            }
        }
        playerJson.add("stats", criteriaJson);

        return playerJson;
    }

    private JsonObject createPreferenceJson(Preferences preferences) {
        JsonObject preferenceJson = new JsonObject();
        preferenceJson.addProperty("teamCount", preferences.propTeamCount().get());

        JsonArray array = new JsonArray();
        for (Preferences.CriteriaUsage usage : preferences.getUsages()) {
            JsonObject usageObject = new JsonObject();
            usageObject.addProperty("criteria", usage.getCriteria().getID().toString());
            usageObject.addProperty("active", usage.isActive());
            usageObject.addProperty("factor", usage.propFactor().get());
            array.add(usageObject);
        }
        preferenceJson.add("usages", array);

        return preferenceJson;
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
