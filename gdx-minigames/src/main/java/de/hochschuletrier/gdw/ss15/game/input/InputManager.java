package de.hochschuletrier.gdw.ss15.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;

public class InputManager {
    
    public static enum InputStates {
        KEYBOARD_MOUCE, GAMEPAD
    }
    
    private static InputStates activeState = InputStates.KEYBOARD_MOUCE;
    private static Controller controller;
    
    private static final InputInterceptor impKeyMouce = new InputInterceptor(new InputKeyboard());;
    private static final InputGamePad impGamepad = new InputGamePad();;


    public static void init() {
        if(controller == null && Controllers.getControllers() != null && Controllers.getControllers().size > 0) {
            InputManager.controller = Controllers.getControllers().first();
        }
        
        try {
            setInputActive(activeState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setActiveSate(InputStates activeState) {
        InputManager.activeState = activeState;
    }
    
    public static Vector2 getViewDirection(Vector2 playerOnScreen) {
            switch (activeState) {
                case KEYBOARD_MOUCE: 
                    return new Vector2(Gdx.input.getX() - playerOnScreen.x, Gdx.input.getY() - playerOnScreen.y);
                case GAMEPAD:
                    if (controller != null){
                        float zitterKreisJoystick = 0.02f;
                        
                        float tmpX = controller.getAxis(3);
                        if (tmpX < zitterKreisJoystick && tmpX > -zitterKreisJoystick) {
                            tmpX = 0;
                        }
                        
                        float tmpY = controller.getAxis(4);
                        if (tmpY < zitterKreisJoystick && tmpY > -zitterKreisJoystick) {
                            tmpY = 0;
                        }
                        return new Vector2(tmpX, tmpY);
                    }
                default:
                    return null;
            }
        
    }
    
    public static void setInputActive (InputStates input) throws Exception {
        
        if (input == InputStates.GAMEPAD && controller == null) {
            throw new Exception("kein Controller");
        }
        
        // deactivate active input
        
            switch (activeState) {
                case KEYBOARD_MOUCE: 
                    impKeyMouce.setActive(false);
                    break;
                case GAMEPAD:
                    controller.removeListener(impGamepad);
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
                controller.addListener(impGamepad);
                break;
            default: 
                throw new Exception("Input nicht unterstützt"); 
        }
        
        activeState = input;
    }
    
    public static void setController(Controller controller) {
        InputManager.controller = controller;
    }
    
    public static Controller getController() {
        return controller;
    }
    
    public static InputInterceptor getInputProcessor() {
        return impKeyMouce;
    }
}
