package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class NotReceptiveComponent extends Component implements Pool.Poolable {

    public float time;
    public float remainingTime;
    public boolean isStunned;
    
    @Override
    public void reset() {
        time =-1;
        remainingTime =-1;
        isStunned=false;
    }

}
