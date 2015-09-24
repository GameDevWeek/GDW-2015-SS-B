package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from client only
 */
public class ShootDatagram extends NetDatagram {

    private final Vector2 direction = new Vector2();

    public static ShootDatagram create(Entity entity, Vector2 direction) {
        ShootDatagram datagram = DatagramFactory.create(ShootDatagram.class);
        datagram.direction.set(direction);
        return datagram;
    }

    public Vector2 getDirection() {
        return direction;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putFloat(direction.x);
        message.putFloat(direction.y);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        direction.x = message.getFloat();
        direction.y = message.getFloat();
    }
}
