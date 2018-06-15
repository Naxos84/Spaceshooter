package io.github.naxos84.spaceshooter.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class KeyboardController implements InputProcessor {

    private boolean left;
    private boolean up;
    private boolean right;
    private boolean down;
    private boolean space;
    private boolean escape;
    private boolean numPad0;

    public boolean isLeftPressed() {
        return left;
    }

    public boolean isUpPressed() {
        return up;
    }

    public boolean isRightPressed() {
        return right;
    }

    public boolean isDownPressed() {
        return down;
    }

    public boolean isSpacePressed() {
        return space;
    }

    public boolean isEscapePressed() {
        return escape;
    }

    public boolean isNumpad0Pressed() {
        return numPad0;
    }

    @Override
    public boolean keyDown(final int keycode) {
        boolean keyProcessed = false;
        switch (keycode) {
            case Keys.LEFT:
                left = true;
                keyProcessed = true;
                break;
            case Keys.RIGHT:
                right = true;
                keyProcessed = true;
                break;
            case Keys.UP:
                up = true;
                keyProcessed = true;
                break;
            case Keys.DOWN:
                down = true;
                keyProcessed = true;
                break;
            case Keys.SPACE:
                space = true;
                keyProcessed = true;
                break;
            case Keys.ESCAPE:
                escape = true;
                keyProcessed = true;
                break;
            case Keys.NUMPAD_0:
                numPad0 = true;
                keyProcessed = true;
                break;
            default:
        }
        return keyProcessed;
    }

    @Override
    public boolean keyUp(final int keycode) {
        boolean keyProcessed = false;
        switch (keycode) {
            case Keys.LEFT:
                left = false;
                keyProcessed = true;
                break;
            case Keys.RIGHT:
                right = false;
                keyProcessed = true;
                break;
            case Keys.UP:
                up = false;
                keyProcessed = true;
                break;
            case Keys.DOWN:
                down = false;
                keyProcessed = true;
                break;
            case Keys.SPACE:
                space = false;
                keyProcessed = true;
                break;
            case Keys.ESCAPE:
                escape = false;
                keyProcessed = true;
                break;
            case Keys.NUMPAD_0:
                numPad0 = false;
                keyProcessed = true;
                break;
            default:
        }
        return keyProcessed;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        return false;
    }
}
