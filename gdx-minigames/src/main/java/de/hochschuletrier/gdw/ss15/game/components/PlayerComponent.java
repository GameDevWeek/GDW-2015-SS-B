package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

// Just a marker component
public class PlayerComponent extends Component implements Pool.Poolable {

    public String name;
    public boolean hasBall;
    
    @Override
    public void reset() {
        name = null;
        hasBall = false;
    }
}
