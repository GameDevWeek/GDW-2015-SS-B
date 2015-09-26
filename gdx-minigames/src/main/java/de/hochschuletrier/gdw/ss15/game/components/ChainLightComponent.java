package de.hochschuletrier.gdw.ss15.game.components;

import box2dLight.ChainLight;
import com.badlogic.ashley.core.Component;

import com.badlogic.gdx.utils.Pool;

public class ChainLightComponent extends Component implements Pool.Poolable {

    public ChainLight chainLight;

    @Override
    public void reset() {
        chainLight.remove();
        chainLight = null;
    }

}
