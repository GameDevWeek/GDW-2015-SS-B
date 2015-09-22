package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;

/**
 * send from server only
 */
public class MoveDatagram extends NetDatagram {

    private long netId;
    private final Vector2 position = new Vector2();
    private final Vector2 velocity = new Vector2();

    public static MoveDatagram create(Entity entity) {
        MoveDatagram datagram = DatagramFactory.create(MoveDatagram.class);
        datagram.netId = entity.getId();
        PositionComponent position = ComponentMappers.position.get(entity);
        datagram.position.set(position.x, position.y);
        final PhysixBodyComponent physixBody = ComponentMappers.physixBody.get(entity);
        Vector2 velocity = physixBody.getLinearVelocity();
        datagram.velocity.set(velocity.x, velocity.y);
        return datagram;
    }

    public long getNetId() {
        return netId;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(netId);
        message.putFloat(position.x);
        message.putFloat(position.y);
        message.putFloat(velocity.x);
        message.putFloat(velocity.y);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        netId = message.getLong();
        position.x = message.getFloat();
        position.y = message.getFloat();
        velocity.x = message.getFloat();
        velocity.y = message.getFloat();
    }
}
