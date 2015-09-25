/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;

/**
 *
 * @author schillen
 */
public class MenuPageHostServer extends MenuPage {
    
    public MenuPageHostServer(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        //addActor(new DecoImage(assetManger.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        int width = 300;
        int height = 50;
        
        createLabel(50, 50, 300, 50, "Enter Server IP");
        TextField serverIP = createTextField(50, 100, 300, 50, "111.111.111.111");
        createLabel(50, 50, 300, 50, "Enter Server Port");
        TextField serverPort = createTextField(50, 100, 300, 50, "");
        createLabel(50, 50, 300, 50, "Enter Username");
        TextField username = createTextField(50, 100, 300, 50, "default");
        createLabel(50, 50, 300, 50, "Select Map");
        TextField mapname = createTextField(50, 100, 300, 50, "default");
        //addCenteredButton(..., this::joinServer); 
        

    }
    
    //ip
        //port
        //username
        //mapname
        //connet -> join server
}
