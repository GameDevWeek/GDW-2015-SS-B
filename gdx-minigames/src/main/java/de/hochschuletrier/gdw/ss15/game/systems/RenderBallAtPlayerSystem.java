/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;


public class RenderBallAtPlayerSystem extends IteratingSystem {
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
                emitter.start();
                emitter.setPosition(pos.x, pos.y);
                emitter.update(deltaTime);
                emitter.draw(DrawUtil.batch);
            }
        }
    }

//    @Override
//    public void onChangeBallOwnershipEvent(Entity owner) {
//        for(ParticleEmitter emitter: effect.getEmitters()){
//            emitter.reset();
//        }
//    }
    
}
