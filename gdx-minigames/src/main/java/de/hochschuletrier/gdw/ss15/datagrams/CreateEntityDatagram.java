package de.hochschuletrier.gdw.ss15.datagrams;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 * send from server only
 */
public class CreateEntityDatagram extends NetDatagram {

    private long netId;
    private String entityType;
    private final Vector2 position = new Vector2();
    private Team team;

    public static CreateEntityDatagram create(Entity entity) {
        CreateEntityDatagram datagram = DatagramFactory.create(CreateEntityDatagram.class);
        datagram.netId = entity.getId();
        final SetupComponent setup = ComponentMappers.setup.get(entity);
        datagram.entityType = setup.name;
        datagram.team = setup.team;
        PositionComponent position = ComponentMappers.position.get(entity);
        datagram.position.set(position.x, position.y);
        return datagram;
    }

    public long getNetId() {
        return netId;
    }

    public String getEntityType() {
        return entityType;
    }

    public Vector2 getPosition() {
        return position;
    }
    
    public Team getTeam() {
        return team;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(netId);
        message.putString(entityType);
        message.putFloat(position.x);
        message.putFloat(position.y);
        message.putEnum(team);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        netId = message.getLong();
        entityType = message.getString();
        position.x = message.getFloat();
        position.y = message.getFloat();
        team = message.getEnum(Team.class);
    }
}
