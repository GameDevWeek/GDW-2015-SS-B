package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputKeyboard implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W: System.out.println("OBEN"); break;
			case Input.Keys.S: System.out.println("UNTEN"); break;
			case Input.Keys.A: System.out.println("LINKS"); break;
			case Input.Keys.D: System.out.println("RECHTS"); break;
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
		System.out.println(screenX + " : " + screenY + " : " + pointer + " : " + button);
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
