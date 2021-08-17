package com.hatit.visual.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.StringProperty;

public class StringToDoubleBinding extends DoubleBinding implements InvalidationListener {

    //_______________________________________________ Parameters
    private final StringProperty textProperty;

    //_______________________________________________ Initialize
    public StringToDoubleBinding(StringProperty textProperty) {
        this.textProperty = textProperty;
        textProperty.addListener(this);
    }
    //_______________________________________________ Methods
    @Override
    public void dispose() {
        textProperty.removeListener(this);
    }

    @Override
    public void invalidated(Observable observable) {
        invalidate();
    }

    @Override
    protected double computeValue() {
        return Double.parseDouble(textProperty.get());
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
