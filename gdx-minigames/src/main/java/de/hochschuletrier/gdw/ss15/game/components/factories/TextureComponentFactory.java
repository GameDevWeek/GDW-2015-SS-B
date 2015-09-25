/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.TextureComponent;


public class TextureComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return ("Texture");
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        TextureComponent component = engine.createComponent(TextureComponent.class);
        component.texture = assetManager.getTexture(properties.getString("texture"));
        if (properties.getString("shadow", null) != null) {
            component.bUseShadow = true;
            component.shadowTexture = assetManager.getTexture(properties.getString("shadow"));
            assert (component.shadowTexture != null);
        } else {
            component.bUseShadow = false;
        }
        assert (component.texture != null);

        component.scale = properties.getFloat("scale", 1.0f);
        entity.add(component);
    }

}
