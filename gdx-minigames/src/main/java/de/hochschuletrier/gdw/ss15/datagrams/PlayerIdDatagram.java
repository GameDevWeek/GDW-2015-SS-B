package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from server only
 */
public final class PlayerIdDatagram extends NetDatagram {

    private long playerId;

    public static PlayerIdDatagram create(long playerId) {
        PlayerIdDatagram datagram = DatagramFactory.create(PlayerIdDatagram.class);
        datagram.playerId = playerId;
        return datagram;
    }

    public long getPlayerId() {
        return playerId;
    }
    
    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(playerId);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        playerId = message.getLong();
    }
}
