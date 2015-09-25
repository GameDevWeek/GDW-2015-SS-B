package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.events.BallDropEvent;
import de.hochschuletrier.gdw.ss15.game.components.BallDropComponent;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.NotReceptiveComponent;

public class BallDropSystem extends IteratingSystem {

    private final float stunningTime = 5;
    private PooledEngine engine;

    public BallDropSystem() {
        this(0);
    }

    public BallDropSystem(int priority){
        super(Family.all(BallDropComponent.class).get(),priority);
        
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = (PooledEngine) engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        this.engine = null;
    }

    @Override
    protected void processEntity(Entity player, float deltaTime) {
        // TODO Auto-generated method stub

        Vector2 dir = new Vector2(
                -(player.getComponent(InputBallComponent.class).move.x),
                -(player.getComponent(InputBallComponent.class).move.y));
        BallDropEvent.emit(player, dir);
        NotReceptiveComponent notReceptive = engine
                .createComponent(NotReceptiveComponent.class);
        notReceptive.remainingTime = stunningTime;
        notReceptive.time = stunningTime;
        notReceptive.isStunned = true;
        // myEntity.remove(InputBallComponent.class);//nicht erwünscht besser
        // mit:
        InputBallComponent input =player.getComponent(InputBallComponent.class);
        input.isStunned = true;
        player.add(input);
        player.add(notReceptive);
        
        player.remove(BallDropComponent.class);

    }

}
