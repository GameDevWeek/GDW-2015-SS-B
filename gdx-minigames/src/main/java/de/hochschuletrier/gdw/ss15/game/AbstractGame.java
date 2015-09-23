package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SoundSystem;
import de.hochschuletrier.gdw.ss15.game.systems.StateRelatedAnimationsRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.TextureRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import java.util.function.Consumer;

public abstract class AbstractGame {

    protected final PooledEngine engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE, GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE);
    protected final EntityFactoryParam factoryParam = new EntityFactoryParam();
    protected final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", TestGame.class);
    protected final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    protected final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE, GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);
    protected final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    protected final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);
    protected final SmoothCamera camera = new SmoothCamera();
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
        Main.getInstance().removeScreenListener(camera);
    }

    public void init(AssetManagerX assetManager, String mapName) {
    	this.mapName = mapName;
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        addSystems();
        addContactListeners();
        entityFactory.init(engine, assetManager);
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Main.getInstance().addScreenListener(camera);
    }

    public void start() {
    }

    protected void addSystems() {
        if(factoryParam.allowPhysics) {
            engine.addSystem(physixSystem);
            engine.addSystem(physixDebugRenderSystem);
        }
        engine.addSystem(new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(new TextureRenderSystem());
        engine.addSystem(new StateRelatedAnimationsRenderSystem());
        engine.addSystem(new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1));
        engine.addSystem(new SoundSystem(camera));
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        addContactListeners(contactListener);
    }

    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
    }

    public void update(float delta) {
        camera.setDestination(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        camera.update(delta);
        camera.bind();
        engine.update(delta);
    }

    public void createTrigger(float x, float y, float width, float height, Consumer<Entity> consumer) {
        Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);
        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        entity.add(triggerComponent);
        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);
    }

    public Entity createEntity(String name, float x, float y) {
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);
        engine.addEntity(entity);
        return entity;
    }
}