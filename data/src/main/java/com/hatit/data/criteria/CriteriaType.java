package com.hatit.data.criteria;

import java.util.function.Supplier;

public enum CriteriaType {
    //_______________________________________________ Parameters
    // Like male/female, team
    TAGGING("Tagging", TaggingSettingImpl::new),

    // like player kill or stamina
    CONSTRAINED_RATING("Vorgegebene Wertung", ConstrainedRatingSettingImpl::new),

    // like size
    OPEN_RATING("Freie Wertung", OpenRatingSettingImpl::new);

    //_______________________________________________ Initialize
    private final String name;
    private final Supplier<Setting> settingsCreator;

    CriteriaType(String name, Supplier<Setting> settingsCreator) {
        this.name = name;
        this.settingsCreator = settingsCreator;
    }

    //_______________________________________________ Methods
    Setting createSetting() {
        return settingsCreator.get();
    }

    public String getName() {
        return name;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}