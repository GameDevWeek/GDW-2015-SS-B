package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

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
	public boolean axisMoved(Controller controller, int axisCode, float value) {
        if(value>0.2 || value <-0.2)
        {
            switch(axisCode){
                case 0: InputPuffer.horizontal = 1.0f*value; break;
                case 1: InputPuffer.vertical = 1.0f*value; break;
            }   
        }
		return false;
	}
}
