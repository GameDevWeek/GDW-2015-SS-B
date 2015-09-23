/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TextureComponent;

/**
 *
 * @author goertzm <goertzm at hs-trier.de>
 */
public class TextureRenderSystem extends IteratingSystem {

    public TextureRenderSystem() {
        this(0);
    }
    
    public TextureRenderSystem(int priority){
        super(Family.all(PositionComponent.class,TextureComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        TextureComponent textureComponent = ComponentMappers.texture.get(entity);
        
        float posX, posY;
        posX = position.x - textureComponent.texture.getWidth() / 2;
        posY = position.y - textureComponent.texture.getHeight() / 2; 
        DrawUtil.rotate(position.x,position.y, position.rotation);
        DrawUtil.draw(textureComponent.texture, posX, posY, 0,  0, (float)textureComponent.texture.getWidth(), (float)textureComponent.texture.getHeight(),1,1,0);
        DrawUtil.rotate(position.x, position.y, -position.rotation);
    }
    
}
