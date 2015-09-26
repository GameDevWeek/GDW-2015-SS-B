package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.datagrams.AnimationStateChangeDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.BallOwnershipChangedDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.CreateEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.MoveDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.RemoveEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.ScoreChangedDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.SoundDatagram;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.events.ScoreChangedEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.components.MovableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

public class NetServerSendSystem extends EntitySystem implements EntityListener,
        ChangeAnimationStateEvent.Listener, SoundEvent.Listener, ChangeBallOwnershipEvent.Listener,
        ScoreChangedEvent.Listener {

    private final NetServerSimple netServer;
    private ImmutableArray<Entity> movables;

    public NetServerSendSystem(NetServerSimple netServer) {
        super(0);

        this.netServer = netServer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(SetupComponent.class).get(), this);
        movables = engine.getEntitiesFor(Family.all(MovableComponent.class, PositionComponent.class, PhysixBodyComponent.class).get());
        ChangeAnimationStateEvent.register(this);
        SoundEvent.register(this);
        ChangeBallOwnershipEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
        movables = null;
        ChangeAnimationStateEvent.unregister(this);
        SoundEvent.unregister(this);
        ChangeBallOwnershipEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity: movables) {
            netServer.broadcastUnreliable(MoveDatagram.create(entity));
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        netServer.broadcastReliable(CreateEntityDatagram.create(entity));
    }

    @Override
    public void entityRemoved(Entity entity) {
        netServer.broadcastReliable(RemoveEntityDatagram.create(entity));
    }

    @Override
    public void onAnimationStateChangedEvent(EntityAnimationState newState, Entity entity) {
        netServer.broadcastReliable(AnimationStateChangeDatagram.create(entity, newState));
    }

    @Override
    public void onSoundEvent(String sound, Entity entity) {
        netServer.broadcastReliable(SoundDatagram.create(sound, entity));
    }

    @Override
    public void onChangeBallOwnershipEvent(Entity owner) {
        netServer.broadcastReliable(BallOwnershipChangedDatagram.create(owner));
    }

    @Override
    public void onScoreChangedEvent(int scoreBlue, int scoreRed) {
        netServer.broadcastReliable(ScoreChangedDatagram.create(scoreBlue, scoreRed));
    }
}
