package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;

public class NetServerSendSystem extends EntitySystem implements EntityListener {

    private final NetServerSimple netServer;
//    private ImmutableArray<Entity> players;

    public NetServerSendSystem(NetServerSimple netServer) {
        super(0);

        this.netServer = netServer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(SetupComponent.class).get(), this);
//        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
//        players = null;
    }

    @Override
    public void update(float deltaTime) {
//        netServer.broadcastUnreliable(PlayerUpdatesDatagram.create(players));
    }

    @Override
    public void entityAdded(Entity entity) {
//        netServer.broadcastReliable(CreateEntityDatagram.create(entity));
    }

    @Override
    public void entityRemoved(Entity entity) {
//        netServer.broadcastReliable(RemoveEntityDatagram.create(entity));
    }
}
