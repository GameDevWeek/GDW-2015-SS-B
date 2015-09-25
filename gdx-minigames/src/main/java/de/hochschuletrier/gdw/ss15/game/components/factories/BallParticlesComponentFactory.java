/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class BallParticlesComponentFactory extends ComponentFactory<EntityFactoryParam>{

    @Override
    public String getType() {
        return("ballParticles");
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        BallParticlesComponent component = engine.createComponent(BallParticlesComponent.class);
        component.currentState = EntityAnimationState.BALL_NEUTRAL;
        component.effect = new ParticleEffect(assetManager.getParticleEffect(properties.getString("effect")));
        entity.add(component);
        
    }
    
}
