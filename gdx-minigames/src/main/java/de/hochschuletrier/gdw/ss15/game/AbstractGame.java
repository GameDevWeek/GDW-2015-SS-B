package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.ss15.game.contactlisteners.ClientContactListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LimitedSmoothCameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.BallParticlesRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LightRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SoundSystem;
import de.hochschuletrier.gdw.ss15.game.systems.StateRelatedAnimationsRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.TextureRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;

public abstract class AbstractGame {

    protected final PooledEngine engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE, GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE);
    protected final EntityFactoryParam factoryParam = new EntityFactoryParam();
    protected final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", TestGame.class);
    protected final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    protected final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE, GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);
    protected final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    protected final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);
    private String mapName;

    public AbstractGame() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
        }
    }

    public String getMapName() {
        return mapName;
    }

    public void dispose() {
        togglePhysixDebug.unregister();
    }

    public void init(AssetManagerX assetManager, String mapName) {
        this.mapName = mapName;
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        addSystems(assetManager);
        addContactListeners();
        entityFactory.init(engine, assetManager);
    }

    public void start() {
    }

    protected void addSystems(AssetManagerX assetManager) {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);

        engine.addSystem(new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1));
        engine.addSystem(new TextureRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS + 1));
        engine.addSystem(new StateRelatedAnimationsRenderSystem(GameConstants.PRIORITY_ANIMATIONS + 2));
        final LimitedSmoothCameraSystem camSys = new LimitedSmoothCameraSystem(GameConstants.PRIORITY_CAMERA);
        engine.addSystem(camSys);
        engine.addSystem(new SoundSystem(GameConstants.PRIORITY_CAMERA));
        engine.addSystem(new BallParticlesRenderSystem(GameConstants.PRIORITY_ANIMATIONS - 1));
        engine.addSystem(new LightRenderSystem(camSys.getCamera(), physixSystem, GameConstants.PRIORITY_MAP + 1));
    }

    private void addContactListeners() {
        if(factoryParam.clientPhysix) {
            physixSystem.getWorld().setContactListener(new ClientContactListener());
        } else {
            PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
            physixSystem.getWorld().setContactListener(contactListener);
            addContactListeners(contactListener);
        }
    }

    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
    }

    public void update(float delta) {
        engine.update(delta);
    }
}
