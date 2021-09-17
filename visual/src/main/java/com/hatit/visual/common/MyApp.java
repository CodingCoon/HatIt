package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyApp extends Application {



    //_______________________________________________ Parameters
    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnimatedButton animatedButton = new AnimatedButton("ButtonText", "AmimationText", ResourceUtil.STAR, () -> System.out.println("FIRE"));
        Button imageButton = StyleUtil.createTextedImageButton(ResourceUtil.STAR, "animatedButton", event -> {});

        VBox root = new VBox(animatedButton, imageButton);
        root.setSpacing(1);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("HatIt.css");

        primaryStage.show();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
