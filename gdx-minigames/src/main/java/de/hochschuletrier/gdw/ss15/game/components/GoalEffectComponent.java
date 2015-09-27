/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 *
 * @author rftpool11
 */
public class GoalEffectComponent extends Component implements Pool.Poolable{
    public ParticleEffect effect;
    public boolean started = false;
    public Team team;
    
    @Override
    public void reset() {
       effect.dispose();
       effect = null;
       started = false;
       team = Team.RED;
    }
     
}
