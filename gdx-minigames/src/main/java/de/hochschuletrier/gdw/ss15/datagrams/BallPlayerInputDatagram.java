package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;

/**
 * send from server only
 */
public class BallPlayerInputDatagram extends NetDatagram {

    private long packetId;
    private final Vector2 move = new Vector2();
    private final Vector2 view = new Vector2();

    public static BallPlayerInputDatagram create(Entity entity) {
        BallPlayerInputDatagram datagram = DatagramFactory.create(BallPlayerInputDatagram.class);
        InputBallComponent input = ComponentMappers.input.get(entity);
        datagram.packetId = input.packetId++;
        datagram.view.set(input.view);
        datagram.move.set(input.move);
        return datagram;
    }

    public long getPacketId() {
        return packetId;
    }

    public Vector2 getMove() {
        return move;
    }

    public Vector2 getView() {
        return view;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(packetId);
        message.putFloat(move.x);
        message.putFloat(move.y);
        message.putFloat(view.x);
        message.putFloat(view.y);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        packetId = message.getLong();
        move.x = message.getFloat();
        move.y = message.getFloat();
        view.x = message.getFloat();
        view.y = message.getFloat();
    }
}
