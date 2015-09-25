package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.data.GameState;

/**
 * send from server only
 */
public final class GameStateDatagram extends NetDatagram {

    private GameState gameState;
    private float gameTime;

    public static GameStateDatagram create(GameState gameState, float gameTime) {
        GameStateDatagram datagram = DatagramFactory.create(GameStateDatagram.class);
        datagram.gameState = gameState;
        datagram.gameTime = gameTime;
        return datagram;
    }

    public GameState getGameState() {
        return gameState;
    }

    public float getGameTime() {
        return gameTime;
    }
    
    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putEnum(gameState);
        message.putFloat(gameTime);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        gameState = message.getEnum(GameState.class);
        gameTime = message.getFloat();
    }
}
