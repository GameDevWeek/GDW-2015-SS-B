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
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class BallParticlesRenderSystem extends IteratingSystem implements ChangeAnimationStateEvent.Listener{

    public BallParticlesRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, BallParticlesComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BallParticlesComponent particleComponent = ComponentMappers.ballParticles.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        for(ParticleEmitter emitter :  particleComponent.effect.getEmitters()){
            if(emitter.getName().toLowerCase().equals(particleComponent.currentState.name().toLowerCase())){
                emitter.update(deltaTime);
            }else{
                emitter.reset();
            }
        }
        particleComponent.effect.setPosition(position.x,position.y);
        particleComponent.effect.draw(DrawUtil.batch);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); 
        ChangeAnimationStateEvent.unregister(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine); 
        ChangeAnimationStateEvent.register(this);
    }
    
    

    @Override
    public void onAnimationStateChangedEvent(EntityAnimationState newState, Entity entity) {
       BallParticlesComponent particleComponent = ComponentMappers.ballParticles.get(entity);
       if(particleComponent == null){
            return;
       }
       for(ParticleEmitter emitter :  particleComponent.effect.getEmitters()){
            if(emitter.getName().toLowerCase().equals(newState.name().toLowerCase())){
               particleComponent.currentState = newState;
            }
        }
    }
    
}
