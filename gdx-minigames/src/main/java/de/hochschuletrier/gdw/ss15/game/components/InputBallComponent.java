package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class InputBallComponent extends Component implements Pool.Poolable {

    public final Vector2 move = new Vector2();
    public final Vector2 view = new Vector2();

    public boolean pull;
    public long packetId; // netcode
    public boolean isStunned;
    
    public float notPullable = 0.0f;

    @Override
    public void reset() {
        move.setZero();
        view.setZero();
        pull = false;
        packetId = 0;
        isStunned=false;
        notPullable = 0.0f;
    }
}
