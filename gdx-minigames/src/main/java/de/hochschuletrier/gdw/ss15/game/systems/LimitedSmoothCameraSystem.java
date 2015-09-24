/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 *
 * @author rftpool11
 */
public class LimitedSmoothCameraSystem extends IteratingSystem {
    
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    
    public LimitedSmoothCameraSystem(int priority) {
        super(Family.all(LocalPlayerComponent.class,PositionComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Main.getInstance().addScreenListener(camera);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        Main.getInstance().removeScreenListener(camera);
    }
    
    public void initMap(TiledMap map) {
        camera.setBounds(0, 0, map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        camera.setDestination(position.x , position.y);
        camera.update(deltaTime);
        camera.bind();
    }       

    public LimitedSmoothCamera getCamera() {
        return camera;
    }
}
