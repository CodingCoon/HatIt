package com.hatit.visual.common;

import com.hatit.visual.StyleUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AnimatedButton extends ButtonBase {
    //_______________________________________________ Parameters
    private final String buttonText;
    private final String animationText;
    private final Image image;
    private final Runnable onFire;

    private final DoubleProperty animator = new SimpleDoubleProperty(0);
    private final BooleanProperty fired = new SimpleBooleanProperty(false);

    //_______________________________________________ Initialize
    public AnimatedButton(String buttonText, String animationText, Image image, Runnable onFire) {
        this.buttonText = buttonText;
        this.animationText = animationText;
        this.image = image;
        this.onFire = onFire;

        setPadding(new Insets(1));
        setBackground(StyleUtil.ROSE_BGR);

        getPseudoClassStates().addListener((SetChangeListener<PseudoClass>) change -> pseudoClassChanged());
        animator.addListener((observable, oldValue, newValue) -> requestLayout());
    }

    //_______________________________________________ Methods
    @Override
    public void fire() {
        if (! fired.get()) {
            onFire.run();
            fired.set(true);

            KeyFrame moveToRight = new KeyFrame(Duration.seconds(0.3), "toRight", new KeyValue(animator, 1d));
            KeyFrame wait        = new KeyFrame(Duration.seconds(5)  , "wait"   , new KeyValue(animator, 1d));
            KeyFrame moveToLeft  = new KeyFrame(Duration.seconds(5.3), "toLeft" , new KeyValue(animator, 0d));
            Timeline timeline = new Timeline(moveToRight, wait, moveToLeft);
            timeline.setOnFinished(event -> fired.set(false));
            timeline.play();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AnimatedButtonSkin(this);
    }

    private void pseudoClassChanged() {
        if (getSkin() != null) {
            ((AnimatedButtonSkin) getSkin()).styleChanged(getPseudoClassStates());
        }
    }

    //_______________________________________________ Inner CLasses
    private static final class AnimatedButtonSkin extends SkinBase<AnimatedButton> {
        private static final double DEFAULT_SIZE = 34d;
        private static final double LABEL_PADDING = 8d;

        private final ButtonBehavior buttonBehavior;

        private final ImageView icon;
        private final Label animationLabel;
        private final Label buttonLabel;

        private AnimatedButtonSkin(AnimatedButton control) {
            super(control);
            buttonBehavior = new ButtonBehavior(control);

            icon = new ImageView(control.image);
            animationLabel = new Label(control.animationText);
            animationLabel.setBackground(new Background(new BackgroundFill(StyleUtil.ROSE, null, null)));
            animationLabel.setTextFill(StyleUtil.BLACK);
            animationLabel.setFont(new Font(18));
            animationLabel.setPadding(new Insets(0, LABEL_PADDING, 0 , 0));

            buttonLabel = new Label(control.buttonText);
            buttonLabel.setBackground(new Background(new BackgroundFill(StyleUtil.LIGHT, null, null)));
            buttonLabel.setTextFill(StyleUtil.BLACK);
            buttonLabel.setFont(new Font(18));
            buttonLabel.setPadding(new Insets(0, 0, 0, LABEL_PADDING));

            getChildren().addAll(animationLabel, buttonLabel, icon);
        }

        @Override
        public void dispose() {
            buttonBehavior.dispose();
        }

        @Override
        protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
            AnimatedButton button = getSkinnable();
            button.setClip(new Rectangle(button.getWidth(), button.getHeight()));
            double animationProgress = button.animator.get() * (button.getWidth() - DEFAULT_SIZE);

            double animationWidth = animationLabel.prefWidth(-1);
            double animationX = -animationWidth + animationProgress;
            animationLabel.resizeRelocate(animationX, contentY, animationWidth, contentHeight);

            double iconX = contentX + 6 + animationProgress;
            icon.resizeRelocate(iconX, contentY + 5, DEFAULT_SIZE, DEFAULT_SIZE);

            double labelWidth  = contentWidth - DEFAULT_SIZE;
            double labelX = contentX + DEFAULT_SIZE + animationProgress;
            buttonLabel.resizeRelocate(labelX, contentY, labelWidth, contentHeight);
        }

        @Override
        protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
            double iconWidth = icon.minWidth(-1);
            double buttonWidth = buttonLabel.minWidth(-1);
            return iconWidth + 6 + buttonWidth;
        }

        @Override
        protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
            return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        }

        @Override
        protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
            double iconWidth = icon.prefWidth(-1);
            double animationWidth = animationLabel.prefWidth(-1) + 12;
            double buttonWidth    = buttonLabel.prefWidth(-1) + 6;
            return leftInset + iconWidth + Math.max(animationWidth, buttonWidth) + rightInset;
        }

        @Override
        protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
            return DEFAULT_SIZE;
        }

        @Override
        protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
            return Double.MAX_VALUE;
        }

        @Override
        protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
            return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        }

        @Override
        protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
            return super.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset);
        }

        public void styleChanged(ObservableSet<PseudoClass> pseudoClassStates) {
            boolean hover = pseudoClassStates.contains(PseudoClass.getPseudoClass("hover"));
            boolean armed = pseudoClassStates.contains(PseudoClass.getPseudoClass("armed"));

            if (armed) {
                buttonLabel.setBackground(new Background(new BackgroundFill(StyleUtil.ROSE, null, null)));
                buttonLabel.setTextFill(StyleUtil.LIGHT);
            }
            else if (hover) {
                buttonLabel.setBackground(new Background(new BackgroundFill(StyleUtil.LIGHT_ROSE, null, null)));
                buttonLabel.setTextFill(StyleUtil.BLACK);
            }
            else {
                buttonLabel.setBackground(new Background(new BackgroundFill(StyleUtil.LIGHT, null, null)));
                buttonLabel.setTextFill(StyleUtil.BLACK);
            }

        }
    }

    //_______________________________________________ End
}
