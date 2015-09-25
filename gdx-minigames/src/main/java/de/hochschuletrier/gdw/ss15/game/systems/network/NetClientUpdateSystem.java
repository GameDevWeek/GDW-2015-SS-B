package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.ss15.datagrams.AnimationStateChangeDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.BallOwnershipChangedDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.CreateEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.PlayerIdDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.MoveDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.RemoveEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.SoundDatagram;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.MovableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;
import java.util.HashMap;

public class NetClientUpdateSystem extends EntitySystem implements NetDatagramHandler, NetClientSimple.Listener {

    private final NetClientSimple netClient;
    private PooledEngine engine;
    private final HashMap<Long, Entity> netEntityMap = new HashMap();

    public NetClientUpdateSystem(NetClientSimple netClient) {
        super(0);
        this.netClient = netClient;
    }

    @Override
    public void update(float deltaTime) {
        netClient.update();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        this.engine = (PooledEngine) engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);

        this.engine = null;
    }

    @Override
    public void onDisconnect() {
        DisconnectEvent.emit();
    }

    public void handle(CreateEntityDatagram datagram) {
        final Vector2 position = datagram.getPosition();
        Entity entity = MapLoader.createEntity(engine, datagram.getEntityType(), position.x, position.y, datagram.getTeam());
        netEntityMap.put(datagram.getNetId(), entity);
    }

    public void handle(RemoveEntityDatagram datagram) {
        Entity entity = netEntityMap.get(datagram.getNetId());
        if (entity != null) {
            engine.removeEntity(entity);
        } else {
            //fixme: warn?
        }
    }

    public void handle(PlayerIdDatagram datagram) {
        Entity entity = netEntityMap.get(datagram.getPlayerId());
        if (entity != null) {
            entity.add(engine.createComponent(LocalPlayerComponent.class));
        }
    }

    public void handle(MoveDatagram datagram) {
        Entity entity = netEntityMap.get(datagram.getNetId());
        if (entity != null) {
            MovableComponent movable = ComponentMappers.movable.get(entity);
            if (datagram.getPacketId() > movable.packetId) {
                movable.packetId = datagram.getPacketId();
                PositionComponent position = ComponentMappers.position.get(entity);
                Vector2 pos = datagram.getPosition();
                position.x = pos.x;
                position.y = pos.y;
                position.rotation = datagram.getRotation();
            }
        }
    }

    public void handle(AnimationStateChangeDatagram datagram) {
        Entity entity = netEntityMap.get(datagram.getNetId());
        if (entity != null) {
            ChangeAnimationStateEvent.emit(datagram.getState(), entity);
        }
    }

    public void handle(SoundDatagram datagram) {
        long netId = datagram.getNetId();
        if (netId == 0) {
            SoundEvent.emit(datagram.getName(), null);
        } else {
            Entity entity = netEntityMap.get(datagram.getNetId());
            if (entity != null) {
                SoundEvent.emit(datagram.getName(), entity);
            }
        }
    }

    public void handle(BallOwnershipChangedDatagram datagram) {
        long netId = datagram.getOwnerId();
        if (netId == 0) {
            ChangeBallOwnershipEvent.emit(null);
        } else {
            Entity entity = netEntityMap.get(netId);
            if (entity != null) {
                PlayerComponent player = ComponentMappers.player.get(entity);
                ChangeBallOwnershipEvent.emit(entity);
            }
        }
    }
}
