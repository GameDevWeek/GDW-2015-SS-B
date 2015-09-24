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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.StateRelatedAnimationsComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rftpool11
 */
public class StateRelatedAnimationsRenderSystem extends IteratingSystem implements ChangeAnimationStateEvent.Listener  {
    private static final Logger logger = LoggerFactory.getLogger(StateRelatedAnimationsRenderSystem.class);
     
    public StateRelatedAnimationsRenderSystem(){
        this(0);
    }
    
     public StateRelatedAnimationsRenderSystem(int priority){
         super(Family.all(PositionComponent.class, StateRelatedAnimationsComponent.class).get(),priority);
     }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateRelatedAnimationsComponent animation = ComponentMappers.stateRelatedAnimations.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        AnimationExtended currentAnimation;
        TextureRegion keyFrame;
        int w, h;
        animation.stateTime += deltaTime;
        
        //render shadow if available
        currentAnimation = animation.shadows.get(animation.currentState);
        if(currentAnimation != null){
            keyFrame = currentAnimation.getKeyFrame(animation.stateTime);
            w = keyFrame.getRegionWidth();
            h = keyFrame.getRegionHeight();
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, animation.scale, animation.scale, 0);
        }
        
        //render animation
        currentAnimation = animation.animations.get(animation.currentState);
        if(currentAnimation != null){
            keyFrame = currentAnimation.getKeyFrame(animation.stateTime);
            w = keyFrame.getRegionWidth();
            h = keyFrame.getRegionHeight();
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, animation.scale, animation.scale, position.rotation);
        }else{
            logger.warn(entity.toString() + "has no animation for state " + animation.currentState.name());
        }
       
    }

    @Override
    public void onAnimationStateChangedEvent(EntityAnimationState newState, Entity entity) {
        StateRelatedAnimationsComponent animation = ComponentMappers.stateRelatedAnimations.get(entity);
        animation.currentState = newState;
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        ChangeAnimationStateEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        ChangeAnimationStateEvent.unregister(this);
    }
}
