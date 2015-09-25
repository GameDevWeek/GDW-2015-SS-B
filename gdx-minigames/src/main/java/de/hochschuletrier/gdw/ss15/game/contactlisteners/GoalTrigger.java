package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalShotComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import java.util.function.Consumer;

/**
 *
 * @author Lusito
 */
public class GoalTrigger implements Consumer<Entity> {
    private final Team team;
    private final PooledEngine engine;

    public GoalTrigger(PooledEngine engine, Team team) {
        this.engine = engine;
        this.team = team;
    }

    @Override
    public void accept(Entity entity) {
        BallComponent ball = ComponentMappers.ball.get(entity);
        if(ball != null && !ball.goal) {
            SoundEvent.emit("ball_goal", entity);
            ball.goal = true;
            GoalShotComponent goalShot = engine.createComponent(GoalShotComponent.class);
            goalShot.countdown = 1; // seconds until reset
            goalShot.team = team;
            entity.add(goalShot);
            ChangeAnimationStateEvent.emit(EntityAnimationState.BALL_NEUTRAL, entity);
        }
    }
}
