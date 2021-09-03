package com.hatit.visual.common;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MyApp extends Application {



    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new HBox(new MyCheckBox()), 300, 300);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("HatIt.css");

        primaryStage.show();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
