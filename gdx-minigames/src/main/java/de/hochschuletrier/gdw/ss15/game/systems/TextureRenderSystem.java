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

    public TextureRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float posX, posY;
        PositionComponent position = ComponentMappers.position.get(entity);
        TextureComponent textureComponent = ComponentMappers.texture.get(entity);

        if (textureComponent.bUseShadow) {
            posX = position.x - textureComponent.shadowTexture.getWidth() / 2;
            posY = position.y - textureComponent.shadowTexture.getHeight() / 2;
            DrawUtil.draw(textureComponent.shadowTexture, posX, posY);
        }

        int w = textureComponent.texture.getWidth();
        int h = textureComponent.texture.getHeight();
        posX = position.x - w / 2.0f;
        posY = position.y - h / 2.0f;

        DrawUtil.batch.draw(textureComponent.texture, posX, posY, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation, 0, 0, w, h, false, true);
    }

}
