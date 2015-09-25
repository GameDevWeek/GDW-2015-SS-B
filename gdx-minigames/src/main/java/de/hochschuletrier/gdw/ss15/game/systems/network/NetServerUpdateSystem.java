package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.datagrams.AnimationStateChangeDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.BallOwnershipChangedDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.BallPlayerInputDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.ConnectDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.CreateEntityDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.GameStateDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.PlayerIdDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.PullChangeDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.ShootDatagram;
import de.hochschuletrier.gdw.ss15.datagrams.WorldSetupDatagram;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.components.StateRelatedAnimationsComponent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.GameType;
import de.hochschuletrier.gdw.ss15.game.manager.PlayerSpawnManager;
import de.hochschuletrier.gdw.ss15.game.systems.GameStateSystem;

public class NetServerUpdateSystem extends EntitySystem implements NetDatagramHandler,
        NetServerSimple.Listener, ChangeGameStateEvent.Listener {

    private final NetServerSimple netServer;
    private ImmutableArray<Entity> entities;
    private final GameType gameType;
    private final String mapName;
    private ImmutableArray<Entity> animationEntities;
    private GameState gameState = GameState.WARMUP;
    private PooledEngine engine;
    private final PlayerSpawnManager playerSpawns;

    public NetServerUpdateSystem(PlayerSpawnManager playerSpawns, NetServerSimple netServer, GameType gameType, String mapName) {
        super(0);

        this.playerSpawns = playerSpawns;
        this.netServer = netServer;
        this.gameType = gameType;
        this.mapName = mapName;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = (PooledEngine) engine;
        entities = engine.getEntitiesFor(Family.all(SetupComponent.class).get());
        animationEntities = engine.getEntitiesFor(Family.all(StateRelatedAnimationsComponent.class).get());
        ChangeGameStateEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        entities = null;
        ChangeGameStateEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        netServer.update();
    }

    @Override
    public boolean onConnect(NetConnection connection) {
        Entity player = playerSpawns.spawnPlayer();
        if (player == null) {
            // no free players available
            return false;
        }

        connection.setAttachment(player);
        return true;
    }

    @Override
    public void onDisconnect(NetConnection connection) {
        Entity playerEntity = (Entity) connection.getAttachment();
        if (playerEntity != null) {
            playerSpawns.freePlayer(playerEntity);
        }
    }

    public void sendWorldSetup(NetConnection connection, ConnectDatagram datagram) {
        String playerName = datagram.getPlayerName();
        final Entity playerEntity = (Entity) connection.getAttachment();
        connection.sendReliable(WorldSetupDatagram.create(gameType, mapName, playerName));

        for (Entity entity : entities) {
            connection.sendReliable(CreateEntityDatagram.create(entity));
            PlayerComponent player = ComponentMappers.player.get(entity);
            if(player != null && player.hasBall)
                connection.sendReliable(BallOwnershipChangedDatagram.create(playerEntity));
        }

        for (Entity entity : animationEntities) {
            StateRelatedAnimationsComponent anim = ComponentMappers.stateRelatedAnimations.get(entity);
            connection.sendReliable(AnimationStateChangeDatagram.create(entity, anim.currentState));
        }

        connection.sendReliable(GameStateDatagram.create(gameState, GameStateSystem.getCountdown()));
        connection.sendReliable(PlayerIdDatagram.create(playerEntity.getId()));
    }

    public void handle(ConnectDatagram datagram) {
        NetConnection connection = datagram.getConnection();
        if (connection.isConnected()) {
            Entity playerEntity = (Entity) connection.getAttachment();
            PlayerComponent player = ComponentMappers.player.get(playerEntity);
            player.name = datagram.getPlayerName();
            sendWorldSetup(connection, datagram);
        }
    }
    public void handle(BallPlayerInputDatagram datagram) {
        NetConnection connection = datagram.getConnection();
        if (connection.isConnected()) {
            Entity playerEntity = (Entity) connection.getAttachment();
            InputBallComponent input = ComponentMappers.input.get(playerEntity);
            if(datagram.getPacketId() > input.packetId) {
                input.packetId = datagram.getPacketId();
                input.move.set(datagram.getMove());
                input.view.set(datagram.getView());
                PhysixBodyComponent physBody = ComponentMappers.physixBody.get(playerEntity);
                physBody.setAngle(input.view.angleRad() + ((float)Math.PI / 2.0f));
            }
        }
    }
    
    public void handle(PullChangeDatagram datagram) {
        NetConnection connection = datagram.getConnection();
        if (connection.isConnected()) {
            Entity entity = (Entity) connection.getAttachment();
            if(datagram.getOn())
                PullEvent.emitOn(entity, datagram.getDirection().nor());
            else
                PullEvent.emitOff(entity);
            InputBallComponent input = ComponentMappers.input.get(entity);
            input.pull = datagram.getOn();
        }
    }
    
    public void handle(ShootDatagram datagram) {
        NetConnection connection = datagram.getConnection();
        if (connection.isConnected()) {
            Entity entity = (Entity) connection.getAttachment();
            ShootEvent.emit(entity, datagram.getDirection().nor());
        }
    }

    @Override
    public void onChangeGameStateEvent(GameState newState, float gameTime) {
        this.gameState = newState;
        netServer.broadcastReliable(GameStateDatagram.create(newState, gameTime));
    }
}
