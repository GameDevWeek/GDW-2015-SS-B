package de.hochschuletrier.gdw.ss14.game.components;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class TriggerComponent extends Component implements Pool.Poolable {

    public Consumer<Entity> consumer;

    @Override
    public void reset() {
        consumer = null;
    }
}
