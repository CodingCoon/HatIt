package com.hatit.visual;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StyleUtil {
    //_______________________________________________ Parameters
    public static final Color BEIGE = Color.color(226/256d, 219/255d, 213/255d);
    public static final Color ROSE = Color.color(209/256d, 112/255d, 119/255d);
    public static final Color RED = Color.color(122/256d, 42/255d, 45/255d);
    public static final Color BLUE = Color.color(34/256d, 50/255d, 63/255d);
    public static final Color BLACK = Color.color(0/256d, 12/255d, 20/255d);

    private static final String IMAGE_BUTTON_STYLE_CLASS = "image-button";
    private static final String H0_STYLE = "h0";
    private static final String H1_STYLE = "h1";
    private static final String H2_STYLE = "h2";
    private static final String H3_STYLE = "h3";

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    public static Label h0(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(H0_STYLE);
        return label;
    }

    public static Label h1(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(H1_STYLE);
        return label;
    }

    public static Label h2(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(H2_STYLE);
        return label;
    }

    public static Label h3(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(H3_STYLE);
        return label;
    }

    public static Button createImageButton(Image image, String tooltip, EventHandler<ActionEvent> onFire) {
        Button button = new Button();
        button.getStyleClass().add(IMAGE_BUTTON_STYLE_CLASS);
        button.setOnAction(onFire);
        button.setGraphic(new ImageView(image));
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
