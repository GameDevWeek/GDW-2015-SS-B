package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputKeyboard extends InputAdapter {
    
    private static boolean up;
    private static boolean down;
    private static boolean left;
    private static boolean right;


    private void updateDirections() {
        if(up == down)
            InputPuffer.vertical = 0;
        else if(up)
            InputPuffer.vertical = -1;
        else if(down)
            InputPuffer.vertical = 1;
            
        if(left == right)
            InputPuffer.horizontal = 0;
        else if(left)
            InputPuffer.horizontal = -1;
        else if(right)
            InputPuffer.horizontal = 1;
    }
    
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W: up = true; break;
			case Input.Keys.S: down = true; break;
			case Input.Keys.A: left = true; break;
			case Input.Keys.D: right = true; break;
            default: return true;
		}
        updateDirections();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.W: up = false; break;
			case Input.Keys.S: down = false; break;
			case Input.Keys.A: left = false; break;
			case Input.Keys.D: right = false; break;
            default: return true;
		}
        updateDirections();
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/*
		 * ~ ~ ~ button ~ ~ ~
		 * 	0: left mouse button
		 * 	1: right mouse button
		 * 	2: middle mouse button 
		 */
		switch (button) {
			case 0: InputPuffer.push = true; break;
			case 1: InputPuffer.pull = true; break;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (button) {
			case 1: InputPuffer.pull = false; break;
		}
		return false;
	}
}
