package de.hochschuletrier.gdw.ss15.game.utils;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.game.AbstractGame;

/**
 * 
 * @author tobidot (Tobias Gepp)
 *
 */


public class MapLoader
{    
    private TiledMap tiledMap;
    /**
     * Standard Konstruktor
     */
    public MapLoader()
    {
    }
    
    public TiledMap getTiledMap()
    {
        return tiledMap;
    }

    /**
     * laedt die Map in das Spiel
     * @param game Spielstand fuer das die Entities geladen werden sollen
     * @param filename Name der Mapdatei die geladen werden soll
     */
    public void run(AbstractGame game, String filename,PhysixSystem pSystem)
    {     
        /// Datei auslesen und in tiledMap packen
        try
        {
            tiledMap = new TiledMap( filename );
        }  catch (Exception ex) 
        {
            throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename);
        }
        
        /// Objekte aus tiledMap laden und per Entitycreator im Game erstellen 
        loadObjectsFromMap( pSystem,game,tiledMap );
    }

    /**
     * Ein Shape zur Physik hinzufuegen
     * @param rect  zu erstellendes Rechteck
     * @param tileWidth breite eines tile
     * @param tileHeight hoehe eines tile
     */
    private void addShape(PhysixSystem pSystem,Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, pSystem).position(x, y).fixedRotation(false);
        Body body = pSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(pSystem).density(1).friction(0.5f).shapeBox(width, height));
    }
    
    /** 
     * Objekte aus tiledMap laden und per Entitycreator im Game erstellen
     * @param game Spiel das gefuellt werden soll
     * @param tiledMap Map die geladen wird
     */     
    private void loadObjectsFromMap(PhysixSystem pSystem,AbstractGame game,TiledMap tiledMap)
    {
        
        
        int mapWidth = tiledMap.getWidth();
        int mapHeight = tiledMap.getHeight();
        int tileWidth = tiledMap.getTileWidth();
        int tileHeight = tiledMap.getTileHeight();
        
        /// Santomagic
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate( tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(pSystem,rect, tileWidth, tileHeight) );
        
        
        /// fuer alles Layers 
        for (Layer layer : tiledMap.getLayers() )
        {
            if( layer.isObjectLayer() )
            {   /// Objects
                for( LayerObject obj : layer.getObjects() )
                {
                    
                    Entity resultEnt;
                    String objectName = obj.getName();
                    float xPos = obj.getX();
                    float yPos = obj.getY();
                    resultEnt = game.createEntity(objectName, xPos, yPos);

                    
                    Consumer<MapSpecialEntities.CreatorInfo> creator = MapSpecialEntities.specialEntities.get( objectName );
                    if ( creator != null )
                    {   /// eine Spezialbehandlung gefunden
                        creator.accept( new MapSpecialEntities.CreatorInfo(resultEnt,tiledMap,obj,layer) );
                    }                        
                }
            } else
            {   /// Tiles
                // Objekte laden die fest in der tiledMap sind
                TileInfo[][] tiles = layer.getTiles();
                for( int x = 0; x < mapWidth; x++ )
                {
                    for( int y = 0; y < mapHeight; y++ )
                    {
                        TileInfo tileInfo = tiles[x][y];
                        if ( tileInfo != null )
                        {
                            /// Name des Tiles bekommen
                            TileSet ts = tiledMap.findTileSet( tileInfo.globalId );
                            String objectName = ts.getName();
                            
                            Entity resultEnt;
                            float xPos = x * tileWidth;
                            float yPos = y * tileHeight;
                            
                            Consumer<MapSpecialEntities.CreatorInfo> creator = MapSpecialEntities.specialEntities.get( objectName );
                            if ( creator != null )
                            {   /// eine Spezialbehandlung gefunden
                                resultEnt = game.createEntity(objectName, xPos, yPos);
                                creator.accept( new MapSpecialEntities.CreatorInfo(resultEnt,x,y,tiledMap, tileInfo ,layer ) );
                            } 
                        }
                    }
                }
            }
        }
        //System.out.println("Map Loaded Succsesful");
    }
    
}
