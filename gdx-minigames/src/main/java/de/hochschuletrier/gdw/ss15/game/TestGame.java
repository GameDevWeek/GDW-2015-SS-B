package de.hochschuletrier.gdw.ss15.game;



import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LimitedSmoothCameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MapRenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;

public class TestGame extends AbstractGame {

    private TiledMap map;

    private void initLoadMap() {
        map = loadMap("data/maps/DummyMap_mit_Entitytypes_Fix.tmx");
        engine.getSystem(LimitedSmoothCameraSystem.class).initMap(map);
        engine.addSystem(new MapRenderSystem(map, GameConstants.PRIORITY_MAP));
    }

    @Override
    public void init(AssetManagerX assetManager, String mapName) {
        super.init(assetManager, mapName);

        MapLoader.entityFactory.init(engine, assetManager);

        this.initLoadMap();

        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map);

        /* TEST SPIELER ERSTELLEN */
        Entity player = MapLoader.createEntity(engine, "player", 100, 100, Team.BLUE);
        player.add(engine.createComponent(LocalPlayerComponent.class));
        
        setupPhysixWorld();

		// Gdx.input.setInputProcessor(new InputKeyboard());
        //
        // Controllers.addListener(new InputGamePad());
    }

    @Override
    protected void addSystems() {
        super.addSystems();
        engine.addSystem(new InputBallSystem(0));
        engine.addSystem(new MovementSystem(1));
    }

    @Override
    protected void addContactListeners(
            PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class,
                new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class,
                new TriggerListener());
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
}
