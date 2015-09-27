package de.hochschuletrier.gdw.ss15.game;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Inputs;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BallListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.GameType;
import de.hochschuletrier.gdw.ss15.game.data.SoundChannel;
import de.hochschuletrier.gdw.ss15.game.input.InputManager;
import de.hochschuletrier.gdw.ss15.game.input.InputManager.InputStates;
import de.hochschuletrier.gdw.ss15.game.manager.BallManager;
import de.hochschuletrier.gdw.ss15.game.manager.PlayerSpawnManager;
import de.hochschuletrier.gdw.ss15.game.systems.BallDropSystem;
import de.hochschuletrier.gdw.ss15.game.systems.GameStateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.GoalEffectRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.GoalShotEventSystem;
import de.hochschuletrier.gdw.ss15.game.systems.HudRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LimitedSmoothCameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MagneticFieldRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MagneticForceSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MapRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ss15.game.systems.PlayerAnimationSystem;
import de.hochschuletrier.gdw.ss15.game.systems.PullSystem;
import de.hochschuletrier.gdw.ss15.game.systems.ReceptiveSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RenderBallAtPlayerSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientSendInputSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetHudRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerSendSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;

public class TestGame extends AbstractGame implements ChangeBallOwnershipEvent.Listener,
        PullEvent.Listener {
    private final NetServerSimple netServer;
    private final NetClientSimple netClient;

    private TiledMap map;
    private final PlayerSpawnManager playerSpawns = new PlayerSpawnManager(engine);

    private BallManager ballManager;
    private ImmutableArray<Entity> players;

    public TestGame() {
        this(null, null);
    }

    public TestGame(NetServerSimple netServer, NetClientSimple netClient) {
        super(netClient != null);
        this.netServer = netServer;
        this.netClient = netClient;
    }
    

    private void initLoadMap(String mapName) {
        map = loadMap(mapName);
        engine.getSystem(LimitedSmoothCameraSystem.class).initMap(map);
        engine.addSystem(new MapRenderSystem(map, GameConstants.PRIORITY_MAP));
        
        InputManager.init();
    }

    @Override
    public void init(AssetManagerX assetManager, String mapName) {
        super.init(assetManager, mapName);
        
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        ChangeBallOwnershipEvent.register(this);
        PullEvent.register(this);

        MapLoader.entityFactory.init(engine, assetManager);

        this.initLoadMap(mapName);

        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map, netClient != null);

        setupPhysixWorld();
        if (netClient == null)
            ballManager = new BallManager(engine);

        if (netServer != null) {
            netServer.setHandler(engine.getSystem(NetServerUpdateSystem.class));
            netServer
                    .setListener(engine.getSystem(NetServerUpdateSystem.class));
        } else if (netClient != null) {
            netClient.setHandler(engine.getSystem(NetClientUpdateSystem.class));
            netClient
                    .setListener(engine.getSystem(NetClientUpdateSystem.class));
        }

        if(netClient == null) {
            /* TEST SPIELER ERSTELLEN */
            Entity player = playerSpawns.spawnPlayer();
            player.add(engine.createComponent(LocalPlayerComponent.class));

            ChangeGameStateEvent.emit(GameState.WARMUP, 0);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void addSystems(AssetManagerX assetManager) {
        super.addSystems(assetManager);
        if(netClient == null) {
            engine.addSystem(new PlayerAnimationSystem(
                    GameConstants.PRIORITY_ENTITIES));
            engine.addSystem(new MovementSystem(10));
            engine.addSystem(new PullSystem(30));
            engine.addSystem(new BallDropSystem(GameConstants.PRIORITY_ENTITIES));
        }
        engine.addSystem(new GoalShotEventSystem(
                GameConstants.PRIORITY_ENTITIES));
        engine.addSystem(new MagneticForceSystem(20));
        engine.addSystem(new ReceptiveSystem(5));
        if(netClient == null)
            engine.addSystem(new GameStateSystem(GameConstants.PRIORITY_ENTITIES));

        if (netServer != null) {
            engine.addSystem(new NetServerSendSystem(netServer));
            engine.addSystem(new NetServerUpdateSystem(playerSpawns, netServer,
                    GameType.MAGNET_BALL, getMapName()));
        } else if (netClient != null) {
            engine.addSystem(new NetClientSendInputSystem(netClient));
            engine.addSystem(new NetClientUpdateSystem(netClient));
        }
        engine.addSystem(new NetHudRenderSystem(assetManager, netClient, netServer, GameConstants.PRIORITY_HUD));
        
        engine.addSystem(new RenderBallAtPlayerSystem(GameConstants.PRIORITY_ANIMATIONS, assetManager));
        
        /* Camera System muss schon existieren */
        engine.addSystem(new InputBallSystem(0, engine.getSystem(LimitedSmoothCameraSystem.class).getCamera()));
        engine.addSystem(new HudRenderSystem(assetManager, GameConstants.PRIORITY_HUD));
        engine.addSystem(new GoalEffectRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(new MagneticFieldRenderSystem(assetManager, GameConstants.PRIORITY_ANIMATIONS-1));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        engine.addSystem(new InputBallSystem(0, engine.getSystem(
                LimitedSmoothCameraSystem.class).getCamera()));
    }

    @Override
    protected void addContactListeners(
            PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class,
                new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class,
                new TriggerListener());
        contactListener.addListener(BallComponent.class, new BallListener(
                engine));
        contactListener.addListener(PlayerComponent.class,
                new PlayerContactListener(engine));
    }

    private void setupPhysixWorld() {
        if(netClient == null)
            physixSystem.setGravity(0, 0);
        else if(!GameConstants.LIGHTS)
            physixSystem.setProcessing(false);
    }

    public static TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: ");

        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (ballManager != null)
            ballManager.dispose();
        if (netClient != null)
            netClient.disconnect();
        else if (netServer != null)
            netServer.disconnect();
        ChangeBallOwnershipEvent.unregister(this);
        PullEvent.unregister(this);
    }

    @Override
    public void onChangeBallOwnershipEvent(Entity owner) {
        for(Entity player: players) {
            final boolean hasBall = owner == player;
            ComponentMappers.player.get(player).hasBall = hasBall;
            PointLightComponent light = ComponentMappers.pointLight.get(player);
            if(light != null && hasBall != light.pointLight.isActive()) {
                light.pointLight.setActive(hasBall);
                if(hasBall) {
                    TeamComponent team = ComponentMappers.team.get(player);
                    light.pointLight.setColor(team.team.color);
                }
            }
        }
    }

    @Override
    public void onPullEventOn(Entity entity, Vector2 direction) {
        if(netClient == null)
            SoundEvent.emit("ball_attract", SoundChannel.LOOP1, entity);
    }

    @Override
    public void onPullEventOff(Entity entity) {
        if(netClient == null)
            SoundEvent.emit(null, SoundChannel.LOOP1, entity);
    }
}
