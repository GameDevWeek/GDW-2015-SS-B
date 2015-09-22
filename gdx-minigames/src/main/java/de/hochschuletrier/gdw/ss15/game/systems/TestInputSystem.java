package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;

public class TestInputSystem extends IteratingSystem {

    public TestInputSystem() {
        super(Family.all(LocalPlayerComponent.class, PhysixBodyComponent.class).get(), 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
        float strengthX = 10000;
        float strengthY = 20000;
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            body.applyImpulse(0, -strengthY);
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            body.applyImpulse(0, strengthY);
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            body.applyImpulse(-strengthX, 0);
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            body.applyImpulse(strengthX, 0);
    }
    
}
