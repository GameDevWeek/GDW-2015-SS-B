package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.game.data.Team;

// Just a marker component
public class GoalShotComponent extends Component implements Pool.Poolable {

    public float countdown;
    public Team team;
    
    @Override
    public void reset() {
        countdown = 0;
        team = null;
    }
}
