package com.morchul.adventuretime.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.morchul.adventuretime.sprites.Player;

public class PlayerInputProcessor implements InputProcessor {

    private Player player;

    public PlayerInputProcessor(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.D: player.right = true; return true;
            case Input.Keys.A: player.left = true; return true;
            case Input.Keys.W: player.up = true; return true;
            case Input.Keys.S: player.down = true; return true;
            case Input.Keys.SPACE: player.jump = true; return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.D: player.right = false; return true;
            case Input.Keys.A: player.left = false; return true;
            case Input.Keys.W: player.up = false; return true;
            case Input.Keys.S: player.down = false; return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.attack = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
