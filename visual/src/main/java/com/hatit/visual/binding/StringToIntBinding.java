package com.hatit.visual.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.StringProperty;

public class StringToIntBinding  extends IntegerBinding implements InvalidationListener {

    //_______________________________________________ Parameters
    private final int defaultValue;
    private final StringProperty stringSource;
    //_______________________________________________ Initialize
    public StringToIntBinding(int defaultValue, StringProperty stringSource) {
        this.defaultValue = defaultValue;
        this.stringSource = stringSource;
        this.stringSource.addListener(this);
    }

    //_______________________________________________ Methods
    @Override
    public void dispose() {
        this.stringSource.removeListener(this);
    }

    @Override
    public void invalidated(Observable observable) {
        invalidate();
    }

    @Override
    protected int computeValue() {
        String text = stringSource.get();
        if (text != null && ! text.isBlank()) {
            try {
                return Integer.parseInt(text);
            }
            catch (NumberFormatException e) {
                System.err.println(e.getCause());
                // some parts are not parseable as int, fallthrough to 0
            }
        }
        return defaultValue;
    }
    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
