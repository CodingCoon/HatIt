package com.hatit.data.criteria;

import java.util.function.Supplier;

public enum CriteriaType {
    //_______________________________________________ Parameters
    // Like male/female
    TAGGING("Tagging", "", TaggingSettingImpl::new),

    // like player kill or stamina
    QUALITATIVE("Qualitativ", "", QualitativeSettingImpl::new),

    // like size
    QUANTITATIVE("Quantitativ", "", QuantitativeSettingImpl::new);

    // like team
    // INFORMATIVE("Informativ", "",  InformativeSettingImpl::new);

    //_______________________________________________ Initialize
    private final String name;
    private final String iconResource;
    private final Supplier<Setting> settingsCreator;

    CriteriaType(String name, String iconResource, Supplier<Setting> settingsCreator) {
        this.name = name;
        this.iconResource = iconResource;
        this.settingsCreator = settingsCreator;
    }

    //_______________________________________________ Methods
    Setting createSetting() {
        return settingsCreator.get();
    }

    public String getName() {
        return name;
    }

    public String getIconResource() {
        return iconResource;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}