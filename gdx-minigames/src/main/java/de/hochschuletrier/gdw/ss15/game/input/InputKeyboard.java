package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputKeyboard implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W: InputPuffer.horizontal = 1.0f; break;
			case Input.Keys.S: InputPuffer.horizontal = -1.0f; break;
			case Input.Keys.A: InputPuffer.vertical = 1.0f; break;
			case Input.Keys.D: InputPuffer.vertical = -1.0f; break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
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
			case 0: InputPuffer.pull = true; break;
			case 1: InputPuffer.push = true; break;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
