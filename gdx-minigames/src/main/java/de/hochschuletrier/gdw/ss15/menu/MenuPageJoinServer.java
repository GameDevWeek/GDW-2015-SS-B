package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author schillen
 */
public class MenuPageJoinServer extends MenuPage {
    
    
    public MenuPageJoinServer(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        //addActor(new DecoImage(assetManger.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        int width = 300;
        int height = 50;
        

        
        //if (type == Type.JOINSERVER) {
        //TextButton addButton1 = addButton(x, y, width, height, "text", this::enterIP, "style");
        //addButton(x, y, width, height, "text", this::enterIP, "style");
            addCenteredButton(x, y - yStep * (i++), 300, 50, "Enter Server IP", this::enterIP);
            //textfeld nachfolgend öffnen und werte uebergabe
        //} else {
            addCenteredButton(x, y - yStep * (i++), 300, 50, "Back To Menu", () -> menuManager.popPage());
        //}
        addCenteredButton(x, y - yStep * (i++), 300, 50, "Exit Client", () -> System.exit(-1));
    }
                
                
    
    
    
    private void enterNickname() {
        //enterNickname.emit();   
    }
    
    private void enterIP() {
        //enterIP.emit();      
    }
    
    private void startServer() {
        String userName = "Server";
        int port = 9090;
        CreateServerEvent.emit(port, GameConstants.MAX_PLAYERS, userName);
    }
}
