package de.hochschuletrier.gdw.ss15.game;


import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;

import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;

import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;

import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;

public class TestGame extends AbstractGame {
	
	private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    private Entity player;
    
    private void initLoadMap() {
    	map = loadMap("data/maps/demo.tmx");        
        
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
    }
    

    @Override
    public void init(AssetManagerX assetManager, String mapName) {
    	super.init(assetManager, mapName);
        
        MapLoader.entityFactory.init(engine, assetManager);
        
        this.initLoadMap();
        
        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map, camera);
        
        setupPhysixWorld();
        
//        Gdx.input.setInputProcessor(new InputKeyboard());
//      
        
        // Controllers.addListener(new InputGamePad());
    }
    

    @Override
    protected void addSystems() {
        super.addSystems();
        engine.addSystem(new InputBallSystem());
    }

    @Override
    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
    }
    
    public static TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Map konnte nicht geladen werden: ");
            
        }
    }
    
    @Override
    public void update(float delta) {
    	mapRenderer.update(delta);
    	// Map wird gerendert !
    	mapRenderer.render(0, 0);
    	
    	super.update(delta);
    	
    	
    	
    }
         
}
