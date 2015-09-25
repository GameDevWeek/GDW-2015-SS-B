package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputPuffer;

public class InputBallSystem extends IteratingSystem {
    
    private final SmoothCamera camera;

    public InputBallSystem(SmoothCamera camera) {
        this(0, camera);
    }

    @SuppressWarnings("unchecked")
    public InputBallSystem(int priority, SmoothCamera camera) {
        super(Family.all(LocalPlayerComponent.class, InputBallComponent.class).get(), priority);
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputBallComponent input = ComponentMappers.input.get(entity);

        // transfer from puffer
        input.move.set(InputPuffer.horizontal, InputPuffer.vertical).nor();
        PositionComponent pos = ComponentMappers.position.get(entity);

        float directionX = Gdx.input.getX() - (pos.x - camera.getLeftOffset());
        float directionY = Gdx.input.getY() - (pos.y - camera.getTopOffset());
        input.view.set(directionX, directionY).nor();
        
        if (input.pull != InputPuffer.pull) { // wechsel hat stattgefunden
            if (InputPuffer.pull) {
                PullEvent.emitOn(entity, input.view);
            } else {
                PullEvent.emitOff(entity);
            }

            input.pull = InputPuffer.pull;
        }

        if (InputPuffer.push) {
            ShootEvent.emit(entity, input.view);
            InputPuffer.push=false;
        }
        
        PhysixBodyComponent physBody = ComponentMappers.physixBody.get(entity);
        if(physBody != null)
            physBody.setAngle(input.view.angleRad() + ((float)Math.PI / 2.0f));
    }
}
