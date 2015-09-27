package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalShotComponent;

public class GoalShotEventSystem extends IteratingSystem {
    private Engine engine;

    public GoalShotEventSystem(int priority) {
        super(Family.all(BallComponent.class, GoalShotComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        this.engine = null;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        GoalShotComponent goalShot = ComponentMappers.goalShot.get(entity);
        goalShot.countdown -= deltaTime;
        if(goalShot.countdown <= 0) {
            GoalEvent.emit(goalShot.team);
            engine.removeEntity(entity);
        }
    }

}
