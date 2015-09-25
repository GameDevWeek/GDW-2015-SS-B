package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class InputKeyboard implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W: InputPuffer.vertical += -1.0f; break;
			case Input.Keys.S: InputPuffer.vertical += 1.0f; break;
			case Input.Keys.A: InputPuffer.horizontal += -1.0f; break;
			case Input.Keys.D: InputPuffer.horizontal += 1.0f; break;
			
			/* Ausprobieren */
            case Input.Keys.I: GoalEvent.emit(Team.RED); break;
			case Input.Keys.O: ChangeGameStateEvent.emit(GameState.GAME); break;
			case Input.Keys.P: GoalEvent.emit(Team.BLUE); break;

		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.W: InputPuffer.vertical += 1.0f; break;
			case Input.Keys.S: InputPuffer.vertical += -1.0f; break;
			case Input.Keys.A: InputPuffer.horizontal += 1.0f; break;
			case Input.Keys.D: InputPuffer.horizontal += -1.0f; break;
		}
		return true;
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
