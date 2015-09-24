/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 *
 * @author rftpool11
 */
public class ParticleRenderSystem extends IteratingSystem{

    public ParticleRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, ParticleEmitterComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleEmitterComponent particleComponent = ComponentMappers.particleEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        PhysixBodyComponent g = ComponentMappers.physixBody.get(entity); 
        particleComponent.effect.update(deltaTime);
        particleComponent.effect.setPosition(position.x,position.y);
        particleComponent.effect.draw(DrawUtil.batch);
        if(particleComponent.effect.isComplete()){
            particleComponent.effect.reset();
            particleComponent.effect.start();
        }
    }
    
}
