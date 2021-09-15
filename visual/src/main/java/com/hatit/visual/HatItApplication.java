package com.hatit.visual;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HatItApplication extends Application {
    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Enviroment enviroment = new Enviroment();

        HatItView root = new HatItView(enviroment);
        Scene scene = new Scene(root, 1000, 1000);
        scene.getStylesheets().add("HatIt.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hat It");
        primaryStage.show();
        primaryStage.showingProperty().addListener((observable, oldValue, newValue) -> shutdown(newValue));
    }

    private void shutdown(Boolean newValue) {
        if (!newValue) {
            System.exit(0);
        }
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
