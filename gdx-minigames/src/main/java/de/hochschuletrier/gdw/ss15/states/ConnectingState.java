package de.hochschuletrier.gdw.ss15.states;

import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.ss15.datagrams.ConnectDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.DatagramFactory;
//import de.hochschuletrier.gdw.ss15.datagrams.WorldSetupDatagram;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.FadeTransition;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.datagrams.PlayerIdDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.WorldSetupDatagram;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.game.NetcodeTestGame;
import java.io.IOException;

/**
 * Connecting to server state
 * 
 * @author Santo Pfingsten
 */
public class ConnectingState extends BaseGameState implements NetDatagramHandler {

    public static enum Status {

        CONNECTING,
        READY,
        DISCONNECTED,
        DONE
    }

    private final Main main;
    private final AssetManagerX assetManager;
    private Status status = Status.CONNECTING;
    private NetcodeTestGame game;
    private final NetClientSimple netClient = new NetClientSimple(DatagramFactory.POOL);
    private NetConnection serverConnection;

    public ConnectingState(AssetManagerX assetManager, String ip, int port, String playerName) throws IOException {
        this.assetManager = assetManager;
        main = Main.getInstance();
        netClient.setHandler(this);
        
        if (netClient.connect(ip, port)) {
            serverConnection = netClient.getConnection();
            serverConnection.sendReliable(ConnectDatagram.create(playerName));
            status = Status.CONNECTING;
        }
    }

    public boolean isSuccess() {
        return status != null;
    }

    public boolean checkForDisconnect() {
        if (!netClient.isRunning()) {
            serverConnection = null;
            status = Status.DISCONNECTED;
            return true;
        }
        return false;
    }

    @Override
    public void update(float delta) {
        if (!netClient.isRunning()) {
            return;
        }
        switch (status) {
            case CONNECTING:
                if(!checkForDisconnect())
                    netClient.update();
                break;
            case READY:
                if(!checkForDisconnect()) {
                    if (!main.isTransitioning()) {
                        GameplayState state = new GameplayState(assetManager, game);
                        game = null;
                        main.changeState(state, new FadeTransition(Color.BLACK, 500), null);
                        status = Status.DONE;
                    }
                }
                break;
            case DISCONNECTED:
                if (!main.isTransitioning()) {
                    if (game != null) {
                        game.dispose();
                        game = null;
                    }
                    BaseGameState state = main.getPersistentState(MainMenuState.class);
                    main.changeState(state, new FadeTransition(Color.BLACK, 500), null);
                }
                break;

        }
        //fixme: draw connection screen
    }
    
    public void handle(WorldSetupDatagram datagram) {
        game = new NetcodeTestGame(null, netClient);
        game.init(assetManager, datagram.getMapName());
        serverConnection = null;
        status = Status.READY;
    }

    @Override
    public void dispose() {
        switch (status) {
            case CONNECTING:
                netClient.disconnect();
                break;
            case READY:
                if (game != null) {
                    game.dispose();
                    game = null;
                }
                break;
        }
        serverConnection = null;
    }
}
