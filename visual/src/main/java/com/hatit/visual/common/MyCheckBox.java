package com.hatit.visual.common;

import com.hatit.visual.StyleUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

public class MyCheckBox extends StackPane {
    //_______________________________________________ Parameters
    private static final double DEFAULT_SIZE = 35;

    private final StackPane hook = new StackPane();

    private final BooleanProperty selected = new SimpleBooleanProperty(true);

    //_______________________________________________ Initialize
    public MyCheckBox() {

        SVGPath hookPath = new SVGPath();
        hookPath.setContent("M9,20.42L2.79,14.21L5.62,11.38L9,14.77L18.88,4.88L21.71,7.71L9,20.42Z");

        hook.setShape(hookPath);
        hook.setBackground(new Background(new BackgroundFill(StyleUtil.LIGHT, null, null)));
        getChildren().add(hook);

        setPadding(new Insets(5));

        setOnMouseClicked(event -> selected.set(! selected.get()));
        setBackground(new Background(new BackgroundFill(StyleUtil.QUEEN_BLUE, null, null)));
        selected.addListener((observable, oldValue, newValue) -> selectionChanged(newValue));
        hoverProperty().addListener((observable, oldValue, newValue) -> updateBackground());
    }

    //_______________________________________________ Methods
    public BooleanProperty propSelected() {
        return selected;
    }

    public boolean isSelected() {
        return propSelected().get();
    }

    @Override
    protected double computePrefWidth(double height) {
        return DEFAULT_SIZE;
    }

    @Override
    protected double computePrefHeight(double width) {
        return DEFAULT_SIZE;
    }

    @Override
    protected double computeMinWidth(double height) {
        return computePrefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height) {
        return computePrefWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return computePrefHeight(width);
    }

    private void updateBackground() {
        boolean hovered = isHover();

        if (hovered || isSelected()) {
            setBackground(new Background(new BackgroundFill(StyleUtil.QUEEN_BLUE, null, null)));
        }
        else {
            setBackground(new Background(
                    new BackgroundFill(StyleUtil.QUEEN_BLUE, null, null),
                    new BackgroundFill(StyleUtil.LIGHT, null, new Insets(1))));
        }
    }

    private void selectionChanged(Boolean isSelected) {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), isSelected);
        hook.setVisible(isSelected);
        updateBackground();
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
