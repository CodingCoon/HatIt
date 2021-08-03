package com.hatit.data.criteria;

public interface InformativeSetting extends Setting<String> {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods

    @Override
    default String getDefaultValue() {
        return null;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
