/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import java.util.Map;

public class StateRelatedAnimationsComponent extends Component implements Pool.Poolable {

    public Map<EntityAnimationState, AnimationExtended> animations;
    public EntityAnimationState currentState =EntityAnimationState.IDLE;
    public float stateTime;
    public int layer;

    @Override
    public void reset() {
        animations.clear();
        stateTime = 0;
        layer = 0;
    }
}

