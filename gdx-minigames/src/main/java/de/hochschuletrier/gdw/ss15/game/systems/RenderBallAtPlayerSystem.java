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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.BallDropEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;


public class RenderBallAtPlayerSystem extends IteratingSystem implements ChangeBallOwnershipEvent.Listener, ShootEvent.Listener, BallDropEvent.Listener{
    ParticleEffect effect;
    
    public RenderBallAtPlayerSystem(int priority, AssetManagerX assetManager) {
        super(Family.all(PositionComponent.class, PlayerComponent.class, TeamComponent.class).get(), priority);
        effect = assetManager.getParticleEffect("player_has_ball");
       
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime){
        PlayerComponent player = ComponentMappers.player.get(entity);
        if(!player.hasBall)return;
        
        PositionComponent pos = ComponentMappers.position.get(entity);
        TeamComponent team = ComponentMappers.team.get(entity);
        for(ParticleEmitter emitter: effect.getEmitters()){
            if(emitter.getName().toLowerCase().equals(team.team.name().toLowerCase())){
                emitter.setPosition(pos.x, pos.y);
                emitter.start();
                emitter.update(deltaTime);
                emitter.draw(DrawUtil.batch);
            }
        }
    }

   
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        ChangeBallOwnershipEvent.unregister(this);
        ShootEvent.unregister(this);
        BallDropEvent.unregister(this);
    }

     @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        ChangeBallOwnershipEvent.register(this);
        ShootEvent.register(this);
        BallDropEvent.register(this);
    }
   
    
   @Override
    public void onChangeBallOwnershipEvent(Entity owner) {
       for(ParticleEmitter emitter: effect.getEmitters()){  
           emitter.reset();
       }       
    }

    @Override
    public void onShootEvent(Entity entity, Vector2 direction) {
        for(ParticleEmitter emitter: effect.getEmitters()){
           emitter.reset();
       } 
    }

    @Override
    public void onDropEvent(Entity entity, Vector2 direction) {
        for(ParticleEmitter emitter: effect.getEmitters()){
           emitter.reset();
       } 
    }
    
}
