package com.hatit.visual.black;

import com.hatit.visual.Enviroment;
import javafx.scene.layout.StackPane;

public class CornerView extends StackPane {
    //_______________________________________________ Parameters
    private static final String ID = "black-view";

    //_______________________________________________ Initialize
    public CornerView(Enviroment enviroment) {
        setId(ID);
        setPrefSize(100, 100);
        // TODO: home button
    }
    //_______________________________________________ Methods
    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
