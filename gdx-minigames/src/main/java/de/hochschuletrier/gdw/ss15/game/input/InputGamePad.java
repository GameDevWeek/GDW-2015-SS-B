package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

public class InputGamePad extends ControllerAdapter {

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
        switch(buttonCode){
            case 6:InputPuffer.pull = true; break;
            case 7:InputPuffer.push = true; break;
        }
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch (buttonCode) {
			case 6: InputPuffer.pull = false; break;
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
	    switch(value) {
	    case center: setMovement(0.0f, 0.0f); break;
	    case north: setMovement(0.0f, -1.0f); break;
	    case northEast: setMovement(0.5f, -0.5f); break;
	    case east: setMovement(1.0f, 0.0f); break;
	    case southEast: setMovement(0.5f, 0.5f); break;
	    case south: setMovement(0.0f, 1.0f); break;
	    case southWest: setMovement(-0.5f, 0.5f); break;
	    case west: setMovement(-1.0f, 0.0f); break;
	    case northWest: setMovement(-0.5f, -0.5f); break;
	    
	    }
	    return true;
	}
	
	private void setMovement(float h, float v) {
	    InputPuffer.horizontal = h;
	    InputPuffer.vertical = v;
	}
}
