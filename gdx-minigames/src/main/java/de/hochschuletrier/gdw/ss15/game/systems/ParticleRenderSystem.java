/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.game.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 *
 * @author rftpool11
 */
public class ParticleRenderSystem extends IteratingSystem{

    public ParticleRenderSystem(int priority) {
        super(Family.all(PositionComponent.class,ParticleEmitterComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
