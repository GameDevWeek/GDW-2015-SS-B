package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;

public class InputManager {
    
    public static enum InputStates {
        KEYBOARD_MOUCE, GAMEPAD
    }
    
    private static InputStates activeState = InputStates.KEYBOARD_MOUCE;
    Array<Controller> controller;
    
    private final InputInterceptor impKeyMouce;
    private final InputGamePad impGamepad;
    
    public InputManager(InputStates inputState) {
        impKeyMouce = new InputInterceptor(new InputKeyboard());
        controller = Controllers.getControllers();
        
        impGamepad = new InputGamePad();
        
        try {
            setInputActive(inputState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Vector2 getViewDirection() {
            switch (activeState) {
                case KEYBOARD_MOUCE: 
                    return new Vector2(Gdx.input.getX(), Gdx.input.getY());
                case GAMEPAD:
                    return new Vector2();
                default:
                    return null;
            }
        
    }
    
    public void setInputActive (InputStates input) throws Exception {
        
        // deactivate active input
        
            switch (activeState) {
                case KEYBOARD_MOUCE: 
                    impKeyMouce.setActive(false);
                    break;
                case GAMEPAD:
                    for (Controller contr : controller) {
                        contr.removeListener(impGamepad);
                    }
                    break;
                default:
                    break;
            }
        
        
        // activate new input
        switch (input) {
            case KEYBOARD_MOUCE:
                impKeyMouce.setActive(true);
                break;
            case GAMEPAD:
                for (Controller contr : controller) {
                    contr.addListener(impGamepad);
                }
                break;
            default: 
                throw new Exception("Input nicht unterstützt"); 
        }
        
        activeState = input;
    }
    
    public InputInterceptor getInputProcessor() {
        return impKeyMouce;
    }
    
    public Array<Controller> getController() {
        return controller;
    }
}
