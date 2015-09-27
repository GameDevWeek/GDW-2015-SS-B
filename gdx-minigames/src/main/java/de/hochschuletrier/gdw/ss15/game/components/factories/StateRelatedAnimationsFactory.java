/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components.factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.StateRelatedAnimationsComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class StateRelatedAnimationsFactory  extends ComponentFactory<EntityFactoryParam>{

    @Override
    public String getType() {
       return("StateRelatedAnimations");
    }
 
    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        StateRelatedAnimationsComponent component = engine.createComponent(StateRelatedAnimationsComponent.class);
        
        if(component.animations == null){
            component.animations = new HashMap<>();
        }
        if(component.shadows == null){
            component.shadows = new HashMap<>();
        }
        String initialState = properties.getString("initial_state", "").toLowerCase();
       
       //check for each state if animation is set
       for(EntityAnimationState state: EntityAnimationState.values()){
          
           //set initial state
           if(state.name().toLowerCase().equals(initialState)){
               component.currentState = state;
           }
           
           //set Animation
           String value = properties.getString("animation_" + state.name().toLowerCase(), null);
           if(value != null){
               AnimationExtended animation = assetManager.getAnimation(value);
               assert (animation != null);
               component.animations.put(state,animation);
           }
           
           //set shadow
           value = properties.getString("animation_" + state.name().toLowerCase() + "_shadow", null);
           if(value != null){
               AnimationExtended animation = assetManager.getAnimation(value);
               assert (animation != null);
               component.shadows.put(state,animation);
           }
           
       }
       if(!properties.keySet().contains("scale")){
           component.scale = 1.0f;
       }else{
           component.scale = properties.getFloat("scale",1.0f);
       } 
       entity.add(component);
    }
    
}
