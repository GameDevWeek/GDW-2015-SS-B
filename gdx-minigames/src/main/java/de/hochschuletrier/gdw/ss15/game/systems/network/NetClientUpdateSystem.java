package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.ss15.datagrams.CreateEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.GameStartDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.MoveDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.RemoveEntityDatagram;
import de.hochschuletrier.gdw.ss15.game.AbstractGame;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import java.util.HashMap;

public class NetClientUpdateSystem extends EntitySystem implements NetDatagramHandler, NetClientSimple.Listener {

    private final NetClientSimple netClient;
    private PooledEngine engine;
    private final HashMap<Long, Entity> netEntityMap = new HashMap();
    private final AbstractGame game;

    public NetClientUpdateSystem(NetClientSimple netClient, AbstractGame game) {
        super(0);
        this.netClient = netClient;
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        netClient.update();
    }

    @Override
    public void onDisconnect() {
//        Main.getInstance().disconnect();
    }

    public void handle(CreateEntityDatagram datagram) {
        System.out.println("test");
        final Vector2 position = datagram.getPosition();
        Entity entity = game.createEntity(datagram.getEntityType(), position.x, position.y);
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

    public void handle(GameStartDatagram datagram) {
    }

    public void handle(MoveDatagram datagram) {
        Entity entity = netEntityMap.get(datagram.getNetId());
        if (entity != null) {
            PositionComponent position = ComponentMappers.position.get(entity);
            Vector2 pos = datagram.getPosition();
            position.x = pos.x;
            position.y = pos.y;
            position.rotation = datagram.getRotation();
        }
    }

}
