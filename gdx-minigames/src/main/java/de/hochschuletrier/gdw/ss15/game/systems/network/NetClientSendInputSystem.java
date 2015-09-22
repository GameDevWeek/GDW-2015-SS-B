package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;

public class NetClientSendInputSystem extends EntitySystem {

    private final NetClientSimple netClient;

    public NetClientSendInputSystem(NetClientSimple netClient) {
        super(0);
        this.netClient = netClient;
    }

    @Override
    public void update(float deltaTime) {
        if (netClient.isRunning()) {
//            Entity localPlayer = game.getLocalPlayer();
            final NetConnection connection = netClient.getConnection();
//            connection.sendUnreliable(PlayerInputDatagram.create(localPlayer));
//            InputComponent input = ComponentMappers.input.get(localPlayer);
//            if (input.dropItem) {
//                connection.sendReliable(DropItemDatagram.create(localPlayer));
//                input.dropItem = false;
//            }
        }
    }
}
