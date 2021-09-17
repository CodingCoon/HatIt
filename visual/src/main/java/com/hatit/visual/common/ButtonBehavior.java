package com.hatit.visual.common;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ButtonBehavior {
    
    //_______________________________________________ Parameters
    private AnimatedButton animatedButton; 
    
    /**
     * Indicates that a keyboard key has been pressed which represents the
     * event (this could be space bar for example). As long as keyDown is true,
     * we are also armed, and will ignore mouse events related to arming.
     * Note this is made package private solely for the sake of testing.
     */
    private boolean keyDown;

    private final InvalidationListener focusListener = this::focusChanged;
    
    //_______________________________________________ Initialize
    public ButtonBehavior(AnimatedButton control) {
        this.animatedButton = control;

        control.setOnKeyPressed(this::keyPressed);
        control.setOnKeyReleased(this::keyReleased);
        control.setOnMouseEntered(this::mouseEntered);
        control.setOnMouseEntered(this::mouseExited);
        control.setOnMousePressed(this::mousePressed);
        control.setOnMouseReleased(this::mouseReleased);

        control.focusedProperty().addListener(focusListener);
    }
    
    //_______________________________________________ Methods
    public void dispose() {
        // TODO specify contract of dispose and post-condition for animatedButton
        animatedButton.focusedProperty().removeListener(focusListener);
    }

    private void focusChanged(Observable o) {
        // If we did have the key down, but are now not focused, then we must
        // disarm the button.
        if (keyDown && !animatedButton.isFocused()) {
            keyDown = false;
            animatedButton.disarm();
        }
    }

    private void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER &&
                ! animatedButton.isPressed() &&
                ! animatedButton.isArmed()) {
            keyDown = true;
            animatedButton.arm();
        }
    }

    private void keyReleased(KeyEvent e) {
        if (keyDown) {
            keyDown = false;
            if (animatedButton.isArmed()) {
                animatedButton.disarm();
                animatedButton.fire();
            }
        }
    }

    private void mousePressed(MouseEvent e) {
        // if the button is not already focused, then request the focus
        if (! animatedButton.isFocused() && animatedButton.isFocusTraversable()) {
            animatedButton.requestFocus();
        }

        // arm the button if it is a valid mouse event
        // Note there appears to be a bug where if I press and hold and release
        // then there is a clickCount of 0 on the release, whereas a quick click
        // has a release clickCount of 1. So here I'll check clickCount <= 1,
        // though it should really be == 1 I think.
        boolean valid = (e.getButton() == MouseButton.PRIMARY &&
                ! (e.isMiddleButtonDown() || e.isSecondaryButtonDown() ||
                        e.isShiftDown() || e.isControlDown() || e.isAltDown() || e.isMetaDown()));

        if (! animatedButton.isArmed() && valid) {
            animatedButton.arm();
        }
    }

    private void mouseReleased(MouseEvent e) {
        // if armed by a mouse press instead of key press, then fire!
        if (! keyDown && animatedButton.isArmed()) {
            animatedButton.fire();
            animatedButton.disarm();
        }
    }

    private void mouseEntered(MouseEvent e) {
        // rearm if necessary
        if (! keyDown && animatedButton.isPressed()) {
            animatedButton.arm();
        }
    }

    private void mouseExited(MouseEvent e) {
        // Disarm if necessary
        if (! keyDown && animatedButton.isArmed()) {
            animatedButton.disarm();
        }
    }
    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
