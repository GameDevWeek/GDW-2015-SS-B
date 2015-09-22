package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SetupComponent extends Component implements Pool.Poolable {

    public String name;

    @Override
    public void reset() {
        name = null;
    }
}
