package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.data.GameType;
import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientSendInputSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetClientUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerSendSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetServerUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.utils.PhysixUtil;

public class NetcodeTestGame extends AbstractGame {
    private final NetServerSimple netServer;
    private final NetClientSimple netClient;

    public NetcodeTestGame(NetServerSimple netServer, NetClientSimple netClient) {
        this.netServer = netServer;
        this.netClient = netClient;
        if(netClient != null)
            factoryParam.allowPhysics = false;
    }
    
    @Override
    public void init(AssetManagerX assetManager, String mapName) {
        super.init(assetManager, mapName);
        setupPhysixWorld();
        if(netClient == null) {
            Entity player = createEntity("player", 300, 300);
            player.add(engine.createComponent(LocalPlayerComponent.class));
        }
        
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
        engine.addSystem(new InputBallSystem());
        
        if(netServer != null) {
            engine.addSystem(new NetServerSendSystem(netServer));
            engine.addSystem(new NetServerUpdateSystem(netServer, GameType.MAGNET_BALL, getMapName()));
        } else if(netClient != null) {
            engine.addSystem(new NetClientSendInputSystem(netClient));
            engine.addSystem(new NetClientUpdateSystem(netClient, this));
        }
    }

    @Override
    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
    }

    private void setupPhysixWorld() {
        if(netClient == null) {
            physixSystem.setGravity(0, 24);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 500).fixedRotation(false);
            Body body = physixSystem.getWorld().createBody(bodyDef);
            body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
            PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);

            createTrigger(410, 600, 3200, 40, (Entity entity) -> {
                engine.removeEntity(entity);
            });
        }
    }
}
