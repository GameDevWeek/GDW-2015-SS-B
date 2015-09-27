/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class BallParticlesComponent extends Component implements Pool.Poolable {
    public ParticleEffect effect;
    public EntityAnimationState currentState;
    
    @Override
    public void reset() {
        effect.dispose();
        currentState = EntityAnimationState.BALL_NEUTRAL;
        effect = null;
    }
    
}
