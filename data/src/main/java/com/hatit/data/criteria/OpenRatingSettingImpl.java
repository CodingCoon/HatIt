package com.hatit.data.criteria;

class OpenRatingSettingImpl implements OpenRatingSetting {
    //_______________________________________________ Parameters
    private String unit = "";

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
