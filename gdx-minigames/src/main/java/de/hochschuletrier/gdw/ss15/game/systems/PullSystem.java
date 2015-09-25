package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class PullSystem extends IteratingSystem {

    float speed = 200f;
    float pullRange = 500f;
    private ImmutableArray<Entity> balls;
    private final float force = 100;

    public PullSystem() {
        this(0);
    }

    public PullSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class,
                PlayerComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        balls = engine.getEntitiesFor(Family.all(BallComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        balls = null;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Entity ball = balls.size() > 0 ? balls.first() : null;
        if (ball != null) {
            InputBallComponent input = entity.getComponent(InputBallComponent.class);
            if (input.pull) {
                Vector2 direction = input.view;
                PositionComponent ballPos = ball.getComponent(PositionComponent.class);
                PositionComponent entPos = entity.getComponent(PositionComponent.class);
                Vector2 differences = new Vector2(ballPos.x - entPos.x, ballPos.y - entPos.y);
                float dirAngle = differences.angle();
                float viewAngle = direction.angle();
                float diff = dirAngle - viewAngle;
                if (diff < -180) {
                    diff += 360;
                } else if (diff > 180) {
                    diff -= 360;
                }
                if (Math.abs(diff) < 30) {
                    float dist = differences.len();
                    if (dist < pullRange) {
                        differences.nor().scl(-force * (pullRange / dist));
                        PhysixBodyComponent physix = ComponentMappers.physixBody.get(ball);
                        physix.simpleForceApply(differences);
                    }
                }
            }

        }
    }
}
