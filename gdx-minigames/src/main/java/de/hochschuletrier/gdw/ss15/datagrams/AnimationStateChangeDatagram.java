package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 * send from server only
 */
public class AnimationStateChangeDatagram extends NetDatagram {

    private long netId;
    private EntityAnimationState state;

    public static AnimationStateChangeDatagram create(Entity entity, EntityAnimationState state) {
        AnimationStateChangeDatagram datagram = DatagramFactory.create(AnimationStateChangeDatagram.class);
        datagram.netId = entity.getId();
        datagram.state = state;
        return datagram;
    }

    public long getNetId() {
        return netId;
    }

    public EntityAnimationState getState() {
        return state;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(netId);
        message.putEnum(state);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        netId = message.getLong();
        state = message.getEnum(EntityAnimationState.class);
    }
}
