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
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 *
 * @author rftpool11
 */
public class GoalEffectRenderSystem extends IteratingSystem implements GoalEvent.Listener {

    public GoalEffectRenderSystem(int priority) {
        super(Family.all(GoalEffectComponent.class, PositionComponent.class).get(), priority);
    }

    @Override
    public void onGoalEvent(Team team) {
        GoalEffectComponent.start = true;
        GoalEffectComponent.current_team = team;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); 
        GoalEvent.unregister(this);
    }

    @Override
    public void addedToEngine(Engine engine){
        super.addedToEngine(engine); 
        GoalEvent.register(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
          GoalEffectComponent component = ComponentMappers.goalEffect.get(entity);
          PositionComponent pos = ComponentMappers.position.get(entity);
          if(component.effect.isComplete() && !component.start){
             return; 
          }
          component.start = false;
          if(component.start){
              component.effect.reset();
              component.effect.start();
          }
          
          for(ParticleEmitter emitter : component.effect.getEmitters()){
              if(emitter.getName().toLowerCase().equals(GoalEffectComponent.current_team.name().toLowerCase())){
                  emitter.setPosition(pos.x,pos.y);
                  emitter.update(deltaTime);
                  emitter.draw(DrawUtil.batch);
              }
          }
    }
    
}
