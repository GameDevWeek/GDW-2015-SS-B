package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BallListener;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticInfluenceComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.data.GameType;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.manager.BallManager;
import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LimitedSmoothCameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MagneticForceSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MapRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ss15.game.systems.PullSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientSendInputSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerSendSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;
import de.hochschuletrier.gdw.ss15.game.manager.PlayerSpawnManager;
import de.hochschuletrier.gdw.ss15.game.manager.TeamManager;
import de.hochschuletrier.gdw.ss15.game.systems.GoalShotEventSystem;
import de.hochschuletrier.gdw.ss15.game.systems.PlayerAnimationSystem;

public class TestGame extends AbstractGame {
    private final NetServerSimple netServer;
    private final NetClientSimple netClient;

    private TiledMap map;
    private final PlayerSpawnManager playerSpawns = new PlayerSpawnManager(engine);
    private final TeamManager teamManager = new TeamManager();
    
    private BallManager ballManager;

    public TestGame() {
        this(null, null);
    }
    
    public TestGame(NetServerSimple netServer, NetClientSimple netClient) {
        this.netServer = netServer;
        this.netClient = netClient;
        if(netClient != null)
            factoryParam.allowPhysics = false;
    }
    
    private void initLoadMap() {
        map = loadMap("data/maps/NiceMap.tmx");
        engine.getSystem(LimitedSmoothCameraSystem.class).initMap(map);
        engine.addSystem(new MapRenderSystem(map, GameConstants.PRIORITY_MAP));
    }

    @Override
    public void init(AssetManagerX assetManager, String mapName) {
        super.init(assetManager, mapName);

        MapLoader.entityFactory.init(engine, assetManager);

        this.initLoadMap();

        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map);

        if(netClient == null) {
            /* TEST SPIELER ERSTELLEN */
            Entity player = playerSpawns.spawnPlayer();
            player.add(engine.createComponent(LocalPlayerComponent.class));
 
            //Nur zum testen der Ballphysik
            Entity ball= MapLoader.createEntity(engine, "ball", 3200, 500, Team.BLUE);
           // ball.add(component)
        }
        
        setupPhysixWorld();
        if(netClient == null)
            ballManager = new BallManager(engine);
        
        if(netServer != null) {
            netServer.setHandler(engine.getSystem(NetServerUpdateSystem.class));
            netServer.setListener(engine.getSystem(NetServerUpdateSystem.class));
        } else if(netClient != null) {
            netClient.setHandler(engine.getSystem(NetClientUpdateSystem.class));
            netClient.setListener(engine.getSystem(NetClientUpdateSystem.class));
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void addSystems() {
        super.addSystems();
        engine.addSystem(new PlayerAnimationSystem(GameConstants.PRIORITY_ENTITIES));
        engine.addSystem(new MovementSystem(1));
        engine.addSystem(new PullSystem(3));
        engine.addSystem(new GoalShotEventSystem(GameConstants.PRIORITY_ENTITIES));
        engine.addSystem(new MagneticForceSystem(2));
        
        if(netServer != null) {
            engine.addSystem(new NetServerSendSystem(netServer));
            engine.addSystem(new NetServerUpdateSystem(playerSpawns, netServer, GameType.MAGNET_BALL, getMapName()));
        } else if(netClient != null) {
            engine.addSystem(new NetClientSendInputSystem(netClient));
            engine.addSystem(new NetClientUpdateSystem(netClient));
        }
        
        /* Camera System muss schon existieren */
        engine.addSystem(new InputBallSystem(0, engine.getSystem(LimitedSmoothCameraSystem.class).getCamera()));
    }

    @Override
    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(BallComponent.class, new BallListener(engine));
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
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
        teamManager.dispose();
        if(ballManager != null)
            ballManager.dispose();
        if(netClient != null)
            netClient.disconnect();
        else if(netServer != null)
            netServer.disconnect();
    }
}
