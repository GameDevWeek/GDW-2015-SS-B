package de.hochschuletrier.gdw.ss15.game.utils;

import de.hochschuletrier.gdw.ss15.game.contactlisteners.GoalTrigger;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import java.util.function.Consumer;

/**
 *
 * @author tobidot (Tobias Gepp)
 *
 */
public class MapLoader {

    private TiledMap tiledMap;

    public final static EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory(
            "data/json/entities.json", Main.class);
    private final static EntityFactoryParam factoryParam = new EntityFactoryParam();

    /**
     * Standard Konstruktor
     */
    public MapLoader() {
    }

    public static Entity createEntity(PooledEngine engine, String name,
            float x, float y, Team team) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.team = team;
        Entity entity = entityFactory.createEntity(name, factoryParam);
        SetupComponent setup = engine.createComponent(SetupComponent.class);
        setup.name = name;
        entity.add(setup);
        engine.addEntity(entity);
        return entity;
    }

    public static void createTrigger(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height, Consumer<Entity> consumer) {
        Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);
        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        entity.add(triggerComponent);
        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x + width/2, y + height/2);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);
    }
    
    public static void generateWorldFromTileMapX(PooledEngine engine,
            PhysixSystem physixSystem, TiledMap map) {
        
        // Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("solid", false),
                (Rectangle rect) -> addShape(physixSystem, rect, tileWidth, tileHeight));
        
        
        for (Layer layer : map.getLayers()) {
            if (layer.isObjectLayer()) {
                // / pre filtering important objects,
                // / wich needs to be already existing when loading other
                // Objects
                for (LayerObject obj : layer.getObjects()) {
                    String entitytype = obj.getProperty("Entitytype", null);
                    if(entitytype == null)
                        continue;
                    entitytype = entitytype.toLowerCase();
                    if (entitytype != null) {
                        
                        int tmp = obj.getIntProperty("Team", -1);
                        Team team;
                        
                        if(tmp == -1) {
                            team = null;
                        }
                        else {
                            team  = (tmp == 0) ? Team.RED : Team.BLUE;
                        }
                         
                        switch (entitytype) {
                            case "goal":
                                createTrigger(engine, physixSystem,
                                        obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(),
                                        new GoalTrigger(engine, team));
                                break;
                            case "magnet":
                            	if(team == Team.BLUE) {
                            		entitytype = "magnet_minus";
                            	}
                            	else {
                            		entitytype = "magnet_plus";
                            	}
                            case "playerspawn":
                            case "ballspawn":
                            	createEntity(engine, entitytype, obj.getX() + obj.getWidth() / 2.0f,
                            			obj.getY() + obj.getHeight() / 2.0f, team
                            			); 
                            	break;
                            default:
                                System.out.println(entitytype + "Nicht bekannt");
                        }
                    }
                }
            }
        }
    }
    
    private static void addShape(PhysixSystem physixSystem, Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(width, height));
    }
}
