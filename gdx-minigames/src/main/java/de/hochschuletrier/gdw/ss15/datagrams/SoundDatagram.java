package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.data.SoundChannel;

/**
 * send from server only
 */
public final class SoundDatagram extends NetDatagram {

    private long netId;
    private String name;
    private SoundChannel channel;

    public static SoundDatagram create(String name, SoundChannel channel, Entity entity) {
        SoundDatagram datagram = DatagramFactory.create(SoundDatagram.class);
        datagram.netId = entity == null ? 0 : entity.getId();
        datagram.name = name;
        datagram.channel = channel;
        return datagram;
    }

    public long getNetId() {
        return netId;
    }

    public SoundChannel getChannel() {
        return channel;
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
        message.putBool(name != null);
        if(name != null)
            message.putString(name);
        message.putEnum(channel);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        netId = message.getLong();
        if(message.getBool())
            name = message.getString();
        else
            name = null;
        channel = message.getEnum(SoundChannel.class);
    }
}
