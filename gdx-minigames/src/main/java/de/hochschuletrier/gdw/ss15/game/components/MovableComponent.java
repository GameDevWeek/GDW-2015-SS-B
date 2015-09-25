package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

// for netcode
public class MovableComponent extends Component implements Pool.Poolable {

    public long packetId;

    @Override
    public void reset() {
        packetId = 0;
    }
}
