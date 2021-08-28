package com.hatit.visual.common;

import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class RatingView extends HBox {
    //_______________________________________________ Parameters
    private static final PseudoClass SELECTED_STATE = PseudoClass.getPseudoClass("selected");
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
            starView.setEffect(StyleUtil.createColorAdjust(StyleUtil.BLUE));
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
