package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from server only
 */
public final class SoundDatagram extends NetDatagram {

    private long netId;
    private String name;

    public static SoundDatagram create(String name, Entity entity) {
        SoundDatagram datagram = DatagramFactory.create(SoundDatagram.class);
        datagram.netId = entity == null ? 0 : entity.getId();
        datagram.name = name;
        return datagram;
    }

    public long getNetId() {
        return netId;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(netId);
        message.putString(name);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        netId = message.getLong();
        name = message.getString();
    }
}
