package de.hochschuletrier.gdw.ss15.game.utils;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.AbstractGame;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.data.Team;

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
    public static void generateWorldFromTileMapX(PooledEngine engine,
            PhysixSystem physixSystem, TiledMap map, SmoothCamera camera) {
        for (Layer layer : map.getLayers()) {
            if (layer.isObjectLayer()) {
				// / pre filtering important objects,
                // / wich needs to be already existing when loading other
                // Objects
                for (LayerObject obj : layer.getObjects()) {
                    String entitytype = obj.getProperty("Entitytype", null);
                    if (entitytype != null) {
                        System.out.println(entitytype.toLowerCase());
                        switch (entitytype.toLowerCase()) {
                            case "box"://fallthrough is intended
//						case "wall":
//						case "magnet":
//							//TEAM
//						case "gate":
//							 //TEAM
//						case "ballspawn":
//							//TEAM
                                Team team = Team.BLUE; // einlesen!
                                createEntity(engine, entitytype.toLowerCase(),
                                        obj.getX() + obj.getWidth() / 2.0f,
                                        obj.getY() + obj.getHeight() / 2.0f,
                                        team);
                                break;//intended
                            default:
                                System.out.println(entitytype + "Nicht bekannt");
                        }
                    }
                }
            }
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * Ein Shape zur Physik hinzufuegen
     *
     * @param rect zu erstellendes Rechteck
     * @param tileWidth breite eines tile
     * @param tileHeight hoehe eines tile
     */
    private void addShape(PhysixSystem pSystem, Rectangle rect, int tileWidth,
            int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                pSystem).position(x, y).fixedRotation(false);
        Body body = pSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(pSystem).density(1)
                .friction(0.5f).shapeBox(width, height));
    }

}
