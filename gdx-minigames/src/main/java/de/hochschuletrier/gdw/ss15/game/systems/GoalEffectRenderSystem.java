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
import de.hochschuletrier.gdw.ss15.events.GoalShotEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 *
 * @author rftpool11
 */
public class GoalEffectRenderSystem extends IteratingSystem implements GoalShotEvent.Listener {
    boolean goalScored = false;
    Team teamScored = Team.RED;
    
    public GoalEffectRenderSystem(int priority) {
        super(Family.all(GoalEffectComponent.class, PositionComponent.class).get(), priority);
    }

    @Override
    public void onGoalShotEvent(Team team) {
        this.goalScored = true;
        this.teamScored = team;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); 
        GoalShotEvent.unregister(this);
    }

    @Override
    public void addedToEngine(Engine engine){
        super.addedToEngine(engine); 
        GoalShotEvent.register(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GoalEffectComponent component = ComponentMappers.goalEffect.get(entity);
        PositionComponent pos = ComponentMappers.position.get(entity);
        TeamComponent team = ComponentMappers.team.get(entity);
        
        if(team.team != this.teamScored) return; //emitter belongs to goal of team who scored
        
        if(!component.started && !this.goalScored) return; //no gool scored and nothing to update
        
        if(component.started && this.goalScored) this.goalScored = false; //all emitters have started
        
        
        for(ParticleEmitter emitter : component.effect.getEmitters()){
            if(emitter.getName().toLowerCase().equals(this.teamScored.name().toLowerCase())){
                //gool scored but emitter not started
                if(!component.started){
                    emitter.setPosition(pos.x,pos.y);
                    emitter.reset();
                    emitter.start();
                    component.started = true;
                }
                emitter.update(deltaTime);
                emitter.draw(DrawUtil.batch);
                //System.out.println(emitter.getPercentComplete());
                if(emitter.isComplete()){
                    component.started = false;
                } 
            }
        }       
    }   
}
