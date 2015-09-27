package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class PlayerAnimationSystem extends IteratingSystem {

    public PlayerAnimationSystem(int priority) {
        super(Family.all(InputBallComponent.class, PhysixBodyComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PhysixBodyComponent physBody = ComponentMappers.physixBody.get(entity);
        InputBallComponent input = ComponentMappers.input.get(entity);

        if(input.isStunned)
            ChangeAnimationStateEvent.emit(getStunnedState(entity), entity);
        else if(physBody.getLinearVelocity().len() >= 10f)
            ChangeAnimationStateEvent.emit(getWalkState(entity), entity);
        else
            ChangeAnimationStateEvent.emit(getIdleState(entity), entity);
    }
    
    private EntityAnimationState getWalkState(Entity entity) {
        Team team = ComponentMappers.team.get(entity).team;
        return team == Team.BLUE ? EntityAnimationState.WALK_MINUS : EntityAnimationState.WALK_PLUS;
    }
    
    private EntityAnimationState getIdleState(Entity entity) {
        Team team = ComponentMappers.team.get(entity).team;
        return team == Team.BLUE ? EntityAnimationState.IDLE_MINUS : EntityAnimationState.IDLE_PLUS;
    }
    
    private EntityAnimationState getStunnedState(Entity entity) {
        Team team = ComponentMappers.team.get(entity).team;
        return team == Team.BLUE ? EntityAnimationState.STUNNED_MINUS : EntityAnimationState.STUNNED_PLUS;
    }
}
