package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.ss15.datagrams.BallPlayerInputDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.PullChangeDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.ShootDatagram;
import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;

public class NetClientSendInputSystem extends IteratingSystem implements PullEvent.Listener, ShootEvent.Listener {

    private final NetClientSimple netClient;

    public NetClientSendInputSystem(NetClientSimple netClient) {
        super(Family.all(LocalPlayerComponent.class).get(), 0);
        this.netClient = netClient;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        ShootEvent.register(this);
        PullEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        
        ShootEvent.unregister(this);
        PullEvent.unregister(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (netClient.isRunning()) {
            final NetConnection connection = netClient.getConnection();
            connection.sendUnreliable(BallPlayerInputDatagram.create(entity));
        }
    }

    @Override
    public void onPullEventOn(Entity entity, Vector2 direction) {
        if (netClient.isRunning()) {
            final NetConnection connection = netClient.getConnection();
            connection.sendReliable(PullChangeDatagram.create(entity, direction));
        }
    }

    @Override
    public void onPullEventOff(Entity entity) {
        if (netClient.isRunning()) {
            final NetConnection connection = netClient.getConnection();
            connection.sendReliable(PullChangeDatagram.create(entity, null));
        }
    }

    @Override
    public void onShootEvent(Entity entity, Vector2 direction) {
        if (netClient.isRunning()) {
            final NetConnection connection = netClient.getConnection();
            connection.sendReliable(ShootDatagram.create(entity, direction));
        }
    }
}
