package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from server only
 */
public final class GameStartDatagram extends NetDatagram {

    private long playerId;
    private long time;

    public static GameStartDatagram create(long playerId, long time) {
        GameStartDatagram datagram = DatagramFactory.create(GameStartDatagram.class);
        datagram.playerId = playerId;
        datagram.time = time;
        return datagram;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getPlayerTime() {
        return time;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(playerId);
        message.putLong(time);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        playerId = message.getLong();
        time = message.getLong();
    }
}
