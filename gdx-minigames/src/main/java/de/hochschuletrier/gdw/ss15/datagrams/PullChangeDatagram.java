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
public class PullChangeDatagram extends NetDatagram {

    private boolean on;
    private final Vector2 direction = new Vector2();

    public static PullChangeDatagram create(Entity entity, Vector2 direction) {
        PullChangeDatagram datagram = DatagramFactory.create(PullChangeDatagram.class);
        datagram.on = direction != null;
        if(datagram.on)
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
        if(on) {
            message.putFloat(direction.x);
            message.putFloat(direction.y);
        }
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        on = message.getBool();
        if(on) {
            direction.x = message.getFloat();
            direction.y = message.getFloat();
        }
    }

    public boolean getOn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
