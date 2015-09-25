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
        
        
        createLabel(50, 50, 300, 50, "Enter Server IP");
        serverIP = createTextField(50, 100, 300, 50, "111.111.111.111");
        createLabel(50, 50, 300, 50, "Enter Server Port");
        serverPort = createTextField(50, 100, 300, 50, "");
        createLabel(50, 50, 300, 50, "Enter Username");
        username = createTextField(50, 100, 300, 50, "default");
        createLabel(50, 50, 300, 50, "Select Map");
        mapname = createTextField(50, 100, 300, 50, "default");
        //addCenteredButton(..., this::joinServer);
        
        //addCenteredButton(x, y - yStep * (i++), 400, 50, "Enter Server IP and Nickname", this::enterIP);
        /*
        addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Starten", this::startServer);
        //addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Beitreten", this::joinServer);
        
        
        addCenteredButton(x, y - yStep * (i++), 400, 50, "Back To Menu", () -> menuManager.popPage());
        
        addCenteredButton(x, y - yStep * (i++), 400, 50, "Exit Client", () -> System.exit(-1));
                */
    }

    private void enterIP() {
        
        
    }

    private void startServer() {
        String userName = "Server";
        int port = 9090;
        CreateServerEvent.emit(port, GameConstants.MAX_PLAYERS, userName);
    }

    private void joinServer() {
        String server = "localhost";
        String userName = "Client";
        int port = 9090;
        JoinServerEvent.emit(server, port, userName);
    }
    
    private void enterNickname() {
        
        //*.emit();      
    }
    
    
}
