/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.menu;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;

/**
 *
 * @author schillen
 */
public class MenuPageHostServer extends MenuPage {
    //private final TextField serverIP;
    private final TextField serverPort;
    private final TextField username;
    private final SelectBox mapname;
    private final DecoImage previewImage;
    private HashMap<String, String> maps;
    

    
    public MenuPageHostServer(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        //addActor(new DecoImage(assetManger.getTexture("menu_bg_root_bottom")));
        final int x2 = 300;
        
        try {
            maps = JacksonReader.readMap("data/json/maps.json", String.class);
            
            for(String name: maps.keySet()) {
                System.out.println(name);
                //get.
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
  
        
        //addActor(new DecoImage(assetManger.getTexture("menu_bg_root_bottom")));
        
        createLabel(50, 390, 600, 80, "Host Server Menu", "big");
        createLabel(50, 300, 300, 50, "Enter Server Port");
        serverPort = createTextField(400, 300, 200, 50, "9090");
        createLabel(50, 250, 300, 50, "Enter Username");
        username = createTextField(400, 250, 200, 50, "Host");
        createLabel(50, 200, 300, 50, "Select Map");
        mapname = selectBox(400, 200, 200, 50, "Select Map");
        mapname.setItems(maps.keySet().toArray());
        previewImage = new DecoImage(getPreviewTexture());
        addActor(previewImage);
        
        mapname.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                updatePreview();
            }
        });
        updatePreview();
        //mapname = createTextField(x2, 250, 300, 50, "Map");
        addLeftAlignedButton(400, 150, 300, 50, "START SERVER", this::startServer);
        //addCenteredButton(..., this::joinServer); 
        
        addLeftAlignedButton(50, 50, 400, 50, "BACK TO MAIN MENU", () -> menuManager.popPage());
        

    }
    
    private Texture getPreviewTexture() {
        String mapName = maps.get((String)mapname.getSelected());
        Texture texture = assetManager.getTexture(mapName);
        if(texture != null)
            return texture;
        return assetManager.getTexture("magnet_plus");
    }
    
    private void updatePreview() {
        float x = 850;
        float y = 275;
        float maxWidth = 256;
        float maxHeight = 256;
        Texture texture = getPreviewTexture();
        previewImage.setTexture(texture);
        float width = texture.getWidth();
        float height = texture.getHeight();
        if(width > height) {
            height = maxHeight * height / width;
            width = maxWidth;
        } else {
            width = maxWidth * width / height;
            height = maxHeight;
        }
        previewImage.setBounds(x - width/2, y - height/2, width, height);
    }

    
    private void startServer() {
        try {
            String userName = this.username.getText();
            int port = Integer.parseInt(this.serverPort.getText());
            String mapName = maps.get((String)this.mapname.getSelected());
            CreateServerEvent.emit(port, GameConstants.MAX_PLAYERS, mapName, userName);
        
        }   catch(NumberFormatException e){}
        
    }
       
}
