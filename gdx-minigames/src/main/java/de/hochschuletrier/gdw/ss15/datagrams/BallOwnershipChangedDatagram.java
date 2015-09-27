package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from server only
 */
public final class BallOwnershipChangedDatagram extends NetDatagram {

    private long ownerId;

    public static BallOwnershipChangedDatagram create(Entity owner) {
        BallOwnershipChangedDatagram datagram = DatagramFactory.create(BallOwnershipChangedDatagram.class);
        datagram.ownerId = (owner == null) ? 0 : owner.getId();
        return datagram;
    }

    public long getOwnerId() {
        return ownerId;
    }
    
    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(ownerId);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        ownerId = message.getLong();
    }
}
