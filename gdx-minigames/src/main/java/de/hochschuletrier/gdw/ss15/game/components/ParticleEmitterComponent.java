/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author rftpool11
 */
public class ParticleEmitterComponent extends Component implements Pool.Poolable {
    public ParticleEffect effect;
    
    @Override
    public void reset() {
        effect.dispose();
        effect = null;
    }
    
}
