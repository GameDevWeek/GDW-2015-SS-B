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

    public TextureRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), priority);
    }

    private void drawTexture(Texture texture, float x, float y, float scale, float rotation) {
        int w = texture.getWidth();
        int h = texture.getHeight();
        x -= w / 2.0f;
        y -= h / 2.0f;

        DrawUtil.batch.draw(texture, x, y, w * 0.5f, h * 0.5f, w, h, scale, scale, rotation, 0, 0, w, h, false, true);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        TextureComponent textureComponent = ComponentMappers.texture.get(entity);

        if (textureComponent.bUseShadow) {
            drawTexture(textureComponent.shadowTexture, position.x, position.y, textureComponent.scale, 0);
        }
        drawTexture(textureComponent.texture, position.x, position.y, textureComponent.scale, position.rotation);
    }

}
