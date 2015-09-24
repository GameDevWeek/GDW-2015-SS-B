/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.ParticleEmitterComponent;

/**
 *
 * @author rftpool11
 */
public class ParticleEmitterComponentFactory extends ComponentFactory<EntityFactoryParam>{

    @Override
    public String getType() {
        return("ParticleEmitter");
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        ParticleEmitterComponent component = engine.createComponent(ParticleEmitterComponent.class);
        component.effect = assetManager.getParticleEffect(properties.getString("effect"));
        component.effect.start();
        component.batch = new SpriteBatch();
        
        entity.add(component);
        
    }
    
}
