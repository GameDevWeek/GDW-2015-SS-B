package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.datagrams.ConnectDatagram;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;

public class NetServerUpdateSystem extends EntitySystem implements NetDatagramHandler, NetServerSimple.Listener {

    private final NetServerSimple netServer;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> players;

    public NetServerUpdateSystem(NetServerSimple netServer) {
        super(0);

        this.netServer = netServer;
    }

//        netServer.setListener(this);
//        netServer.setHandler(this);


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(Family.all(SetupComponent.class).get());
//        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        entities = null;
        players = null;
    }

    @Override
    public void update(float deltaTime) {
        netServer.update();
    }

    @Override
    public boolean onConnect(NetConnection connection) {
//        Entity playerEntity = game.acquireBotPlayer();
//        if (playerEntity == null) {
//            // no free players available
//            return false;
//        }
//        connection.setAttachment(playerEntity);
        return true;
    }

    @Override
    public void onDisconnect(NetConnection connection) {
        Entity playerEntity = (Entity) connection.getAttachment();
        if (playerEntity != null) {
//            game.freeBotPlayer(playerEntity);
        }
    }

    public void sendWorldSetup(NetConnection connection) {
        final Entity playerEntity = (Entity) connection.getAttachment();
//        connection.sendReliable(WorldSetupDatagram.create(game.getMapName(), playerEntity, players));
//        connection.sendReliable(PlayerUpdatesDatagram.create(players));

    }

    public void handle(ConnectDatagram datagram) {
        NetConnection connection = datagram.getConnection();
        if (connection.isConnected()) {
            Entity playerEntity = (Entity) connection.getAttachment();
//            PlayerComponent player = ComponentMappers.player.get(playerEntity);
//            player.name = datagram.getPlayerName();
//            sendWorldSetup(connection);
//            netServer.broadcastReliable(PlayerNameDatagram.create(playerEntity));
        }
    }
}
