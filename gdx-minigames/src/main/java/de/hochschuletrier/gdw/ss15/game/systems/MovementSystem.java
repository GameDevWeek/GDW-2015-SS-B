package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 * Created by isik on 23.09.15.
 */
public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        this(0);
    }

    public MovementSystem(int priority) {
        super(Family.all(InputBallComponent.class, PhysixBodyComponent.class)
                .get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PhysixBodyComponent physBody = ComponentMappers.physixBody.get(entity);
        InputBallComponent input = ComponentMappers.input.get(entity);

        float speed = 500;
        if (ComponentMappers.player.has(entity)) {
            if (entity.getComponent(PlayerComponent.class).hasBall) {
                speed /= 2;
            }

        }
        if (entity.getComponent(InputBallComponent.class).isStunned != true) {
            physBody.setLinearVelocity(input.move.x * speed, input.move.y* speed);
        }
    }
}
