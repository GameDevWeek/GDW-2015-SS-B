package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.ss15.datagrams.BallPlayerInputDatagram;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;

public class NetClientSendInputSystem extends IteratingSystem {

    private final NetClientSimple netClient;

    public NetClientSendInputSystem(NetClientSimple netClient) {
        super(Family.all(LocalPlayerComponent.class).get(), 0);
        this.netClient = netClient;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (netClient.isRunning()) {
            final NetConnection connection = netClient.getConnection();
            connection.sendUnreliable(BallPlayerInputDatagram.create(entity));
            System.out.println("test");
        }
    }
}
