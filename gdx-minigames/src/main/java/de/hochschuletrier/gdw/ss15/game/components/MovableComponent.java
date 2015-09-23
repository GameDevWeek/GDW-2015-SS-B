package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

// Just a marker component
public class MovableComponent extends Component implements Pool.Poolable {

    @Override
    public void reset() {
    }
}
