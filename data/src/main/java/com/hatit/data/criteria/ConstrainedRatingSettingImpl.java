package com.hatit.data.criteria;

class ConstrainedRatingSettingImpl implements ConstrainedRatingSetting {
    //_______________________________________________ Parameters
    private Range range = new Range(1, 5);

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
