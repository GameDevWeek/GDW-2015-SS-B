package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;

/**
 *
 * @author schillen
 */
public class MenuPageJoinServer extends MenuPage {
    private final TextField username;
    private final TextField serverIP;
    private final TextField serverPort;
    
    
    public MenuPageJoinServer(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        //addActor(new DecoImage(assetManger.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        int width = 300;
        int height = 50;
        
        createLabel(50, 390, 600, 80, "Join Server Menu", "big");
        createLabel(50, 300, 300, 50, "Enter Server IP");
        serverIP = createTextField(400, 300, 200, 50, "localhost");
        createLabel(50, 250, 300, 50, "Enter Server Port");
        serverPort = createTextField(400, 250, 200, 50, "9090");
        createLabel(50, 200, 300, 50, "Enter Username");
        username = createTextField(400, 200, 200, 50, "Client");
        addLeftAlignedButton(400, 150, 300, 50, "JOIN SERVER", this::joinServer);
        
        addLeftAlignedButton(50, 50, 400, 50, "BACK TO MAIN MENU", () -> menuManager.popPage());
        

    }
    
    private void joinServer() {
        
        try {
            String server = this.serverIP.getText();
            int port = Integer.parseInt(this.serverPort.getText());
            String userName = this.username.getText();
            JoinServerEvent.emit(server, port, userName);
        }   catch(NumberFormatException e){}
        
    }
}
