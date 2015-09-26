package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.NotReceptiveComponent;

public class ReceptiveSystem extends IteratingSystem{

    public ReceptiveSystem(){
        this(0);
    }
    
    public ReceptiveSystem(int priority){
        super(Family.all(NotReceptiveComponent.class).get(),priority);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        if(entity.getComponent(NotReceptiveComponent.class).remainingTime>0){
            entity.getComponent(NotReceptiveComponent.class).remainingTime-= deltaTime;
        }else{
            //
            InputBallComponent input= entity.getComponent(InputBallComponent.class);
            input.isStunned=false;
            entity.remove(NotReceptiveComponent.class);
            entity.add(input);
        }
    }

}
