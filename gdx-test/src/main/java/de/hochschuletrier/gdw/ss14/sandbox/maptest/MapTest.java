package de.hochschuletrier.gdw.ss14.sandbox.maptest;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.GameConstants;
import de.hochschuletrier.gdw.ss14.game.components.BallComponent;
import de.hochschuletrier.gdw.ss14.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagnetComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagneticFieldComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagneticInfluenceComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss14.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.BallListener;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss14.game.systems.MagneticForceSystem;
import de.hochschuletrier.gdw.ss14.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

/**
 *
 * @author Santo Pfingsten
 */
public class MapTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(MapTest.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );
    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private PhysixBodyComponent playerBody;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    public MapTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(new MagneticForceSystem());
        engine.addSystem(new UpdatePositionSystem());
        
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(BallComponent.class, new BallListener());
    }

    @Override
    public void init(AssetManagerX assetManager) {
        map = loadMap("data/maps/demo.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        // Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(rect, tileWidth, tileHeight));

        //create entities
        // create a simple player ball
        Entity player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(100, 100).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(20);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(BallComponent.class));
        player.add(engine.createComponent(MagneticInfluenceComponent.class));
        MagnetComponent magnetComp = engine.createComponent(MagnetComponent.class);
        magnetComp.positiv = true;
        player.add(magnetComp);
        engine.addEntity(player);
        
        //magnets
        Entity magnet = engine.createEntity();
        PhysixModifierComponent modifyComponent2 = engine.createComponent(PhysixModifierComponent.class);
        magnet.add(modifyComponent2);

        modifyComponent2.schedule(() -> {
        	PhysixBodyComponent magnetBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(500, 100).fixedRotation(true);
            magnetBody.init(bodyDef, physixSystem, magnet);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(60).sensor(true);
            magnetBody.createFixture(fixtureDef);
            magnet.add(magnetBody);
        });
        magnet.add(engine.createComponent(PositionComponent.class));
        magnet.add(engine.createComponent(MagneticFieldComponent.class));
        
        MagnetComponent magnetComp2 = engine.createComponent(MagnetComponent.class);
        magnetComp2.positiv = false;
        magnet.add(magnetComp2);
        engine.addEntity(magnet);

        Entity magnet2 = engine.createEntity();
        PhysixModifierComponent modifyComponent3 = engine.createComponent(PhysixModifierComponent.class);
        magnet2.add(modifyComponent3);

        modifyComponent3.schedule(() -> {
        	PhysixBodyComponent magnetBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(800, 100).fixedRotation(true);
            magnetBody.init(bodyDef, physixSystem, magnet2);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(200).sensor(true);
            magnetBody.createFixture(fixtureDef);
            magnet2.add(magnetBody);
        });
        magnet2.add(engine.createComponent(PositionComponent.class));
        magnet2.add(engine.createComponent(MagneticFieldComponent.class));
        
        MagnetComponent magnetComp3 = engine.createComponent(MagnetComponent.class);
        magnetComp3.positiv = true;
        magnet2.add(magnetComp3);
        engine.addEntity(magnet2);
        
        // Setup camera
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
    }

    private void addShape(Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void update(float delta) {
        camera.bind();
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }
        engine.update(delta);
        
        mapRenderer.update(delta);
        camera.update(delta);

        if(playerBody != null) {
            float speed = 300.0f;
            float velX = 0, velY = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velX -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velX += delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velY -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velY += delta * speed;
            }

            //playerBody.setLinearVelocity(velX, velY);
            playerBody.applyImpulse(velX,velY);
            camera.setDestination(playerBody.getPosition());
        }
    }
}
