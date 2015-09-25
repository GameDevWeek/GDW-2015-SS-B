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
public class MenuPageJoinServer extends MenuPage {
    private final TextField username;
    private final TextField serverIP;
    private final TextField mapname;
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
        
        createLabel(400, 500, 600, 80, "Join Server Menu");
        createLabel(50, 400, 300, 50, "Enter Server IP");
        serverIP = createTextField(400, 400, 300, 50, "localhost");
        createLabel(50, 350, 300, 50, "Enter Server Port");
        serverPort = createTextField(400, 350, 300, 50, "9090t");
        createLabel(50, 300, 300, 50, "Enter Username");
        username = createTextField(400, 300, 300, 50, "Client");
        createLabel(50, 250, 300, 50, "Select Map");
        mapname = createTextField(400, 250, 300, 50, "Map");
        addLeftAlignedButton(400, 200, 300, 50, "Join Server", this::joinServer);
        
        addCenteredButton(50, 100, 400, 50, "Back To Menu", () -> menuManager.popPage());
        addCenteredButton(50, 50, 400, 50, "Exit Client", () -> System.exit(-1));

        //addButton(50, 50,300, 50, "text" this::joinServer, "style");
        
        //addCenteredButton(x, y - yStep * (i++), 400, 50, "Enter Server IP and Nickname", this::enterIP);
        /*
        addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Starten", this::startServer);
        //addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Beitreten", this::joinServer);
        
        
        
                */
    }
    
    private void joinServer() {
        String server = "loclahost";
        try {
            int server = this.serverIP.getText();
            int port = Integer.parseInt(this.serverPort.getText());
            String userName = this.username.getText();
            String mapName = "data/maps/NiceMap.tmx";
            JoinServerEvent.emit(server, port, userName);
        }   catch(NumberFormatException e){}
        
    }
}
