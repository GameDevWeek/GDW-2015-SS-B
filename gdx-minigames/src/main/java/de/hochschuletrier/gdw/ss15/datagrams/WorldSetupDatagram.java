package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.GameType;

/**
 * send from server only
 */
public final class WorldSetupDatagram extends NetDatagram {

    private GameType gameType;
    private GameState gameState;
    private String mapName;
    private String playerName;

    public static WorldSetupDatagram create(GameType gameType, GameState gameState, String mapName, String playerName) {
        WorldSetupDatagram datagram = DatagramFactory.create(WorldSetupDatagram.class);
        datagram.gameType = gameType;
        datagram.gameState = gameState;
        datagram.mapName = mapName;
        datagram.playerName = playerName;
        return datagram;
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getMapName() {
        return mapName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putString(mapName);
        message.putEnum(gameType);
        message.putEnum(gameState);
        message.putString(playerName);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        mapName = message.getString();
        gameType = message.getEnum(GameType.class);
        gameState = message.getEnum(GameState.class);
        playerName = message.getString();
    }
}
