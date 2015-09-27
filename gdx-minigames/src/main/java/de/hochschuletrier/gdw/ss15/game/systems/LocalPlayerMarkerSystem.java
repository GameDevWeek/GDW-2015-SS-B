/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 *
 * @author rftpool11
 */
public class LocalPlayerMarkerSystem extends IteratingSystem {

    Texture texture;
    float rotation;
    final float speed = 50.0f;
    
    public LocalPlayerMarkerSystem(int priority, AssetManagerX assetManager) {
        super(Family.all(LocalPlayerComponent.class, PositionComponent.class).get(), priority);
        texture = assetManager.getTexture("local_player_marker");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = ComponentMappers.position.get(entity);
        rotation += deltaTime * speed; 
        drawTexture(texture,pos.x,pos.y, 1.0f ,rotation);
        
    }
    
    private void drawTexture(Texture texture, float x, float y, float scale, float rotation) {
        int w = texture.getWidth();
        int h = texture.getHeight();
        x -= w / 2.0f;
        y -= h / 2.0f;

        DrawUtil.batch.draw(texture, x, y, w * 0.5f, h * 0.5f, w, h, scale, scale, rotation, 0, 0, w, h, false, true);
    }
   
    
}
