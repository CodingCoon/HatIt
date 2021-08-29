package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class RatingView extends HBox {
    //_______________________________________________ Parameters
    private static final String STYLE_CLASS = "rating-view";

    private final IntegerProperty value = new SimpleIntegerProperty();

    //_______________________________________________ Initialize
    public RatingView(int amount) {
        getStyleClass().add(STYLE_CLASS);
        initUI(amount);
    }

    //_______________________________________________ Methods
    public IntegerProperty propValue() {
        return value;
    }

    private void initUI(int amount) {
        for (int i = 0; i < amount; i++) {
            int viewValue = i;

            ImageView starView = new ImageView(ResourceUtil.STAR);
            disabledProperty().addListener((observable, oldValue, newValue) -> {
                ColorAdjust colorAdjust = newValue ? StyleUtil.createColorAdjust(StyleUtil.LIGHT.darker())
                                                   : StyleUtil.createColorAdjust(StyleUtil.BLUE.darker());
                starView.setEffect(colorAdjust);
            });
            value.addListener((observable, oldValue, newValue) -> updateImageView(starView, viewValue));

            StackPane imagePane = new StackPane(starView);
            imagePane.setOnMouseClicked(event -> value.setValue(viewValue));
            getChildren().add(imagePane);
        }
    }

    private void updateImageView(ImageView starView, int viewValue) {
        boolean selected = value.get() >= viewValue;
        starView.setImage(selected ? ResourceUtil.STAR : ResourceUtil.STAR_OUTLINED);
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
