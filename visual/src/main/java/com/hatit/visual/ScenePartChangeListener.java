package com.hatit.visual;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;

public class ScenePartChangeListener implements ChangeListener<Scene> {
    //_______________________________________________ Parameters
    private final Runnable onShowTask;
    private final Runnable onHideTask;

    //_______________________________________________ Initialize
    public ScenePartChangeListener(Runnable onShowTask, Runnable onHideTask) {
        this.onShowTask = onShowTask;
        this.onHideTask = onHideTask;
    }

    //_______________________________________________ Methods
    @Override
    public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
        if (oldValue != null) {
            onHideTask.run();
        }
        if (newValue != null) {
            onShowTask.run();
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
