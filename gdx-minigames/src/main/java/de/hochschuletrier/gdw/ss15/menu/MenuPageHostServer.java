/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import java.util.HashMap;

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
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        
        
        createLabel(x2, 500, 600, 80, "Host Server Menu");
        //createLabel(50, x2, 300, 50, "Enter Server IP");
        //serverIP = createTextField(x2, 400, 300, 50, "");
        createLabel(50, 350, 300, 50, "Enter Server Port");
        serverPort = createTextField(x2, 350, 300, 50, "9090");
        createLabel(50, 300, 300, 50, "Enter Username");
        username = createTextField(x2, 300, 300, 50, "Host");
        createLabel(50, 250, 300, 50, "Select Map");
        mapname = selectBox(x2, 250, 300, 50, "Select Map");
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
        addLeftAlignedButton(x2, 200, 300, 50, "Server Starten", this::startServer);
        //addCenteredButton(..., this::joinServer); 
        
        addCenteredButton(50, 100, 400, 50, "Back To Menu", () -> menuManager.popPage());
        addCenteredButton(50, 50, 400, 50, "Exit Client", () -> System.exit(-1));

    }
    
    private Texture getPreviewTexture() {
        String mapName = maps.get((String)mapname.getSelected());
        Texture texture = assetManager.getTexture(mapName);
        if(texture != null)
            return texture;
        return assetManager.getTexture("magnet_plus");
    }
    
    private void updatePreview() {
        float x = 800;
        float y = 300;
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
