/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalEffectComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class GoalEffectComponentFactory extends ComponentFactory<EntityFactoryParam>{
    @Override
    public String getType() {
        return("goalEffect");
    }
    
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        GoalEffectComponent component = engine.createComponent(GoalEffectComponent.class);
        component.effect = new ParticleEffect(assetManager.getParticleEffect(properties.getString("effect")));
        component.effect.flipY();
        entity.add(component);    
    }
}
