package de.hochschuletrier.gdw.ss15.game.manager;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.BallSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;

public final class BallManager implements ChangeGameStateEvent.Listener, GoalEvent.Listener {

    ArrayList<Entity> start_spawns = new ArrayList<Entity>();
    ArrayList<Entity> team_0_spawns = new ArrayList<Entity>();
    ArrayList<Entity> team_1_spawns = new ArrayList<Entity>();

    private final PooledEngine engine;
    private final ImmutableArray<Entity> balls;

    public BallManager(PooledEngine engine) {
        this.engine = engine;

        distributeSpawns();
        balls = engine.getEntitiesFor(Family.all(BallComponent.class).get());

        ChangeGameStateEvent.register(this);
        GoalEvent.register(this);

    }

    public void dispose() {
        ChangeGameStateEvent.unregister(this);
        GoalEvent.unregister(this);
    }

    public void distributeSpawns() {

        ImmutableArray<Entity> spawns = engine.getEntitiesFor(Family.all(BallSpawnComponent.class, TeamComponent.class).get());

        for (Entity entity : spawns) {
            TeamComponent team = entity.getComponent(TeamComponent.class);

            if (team.team == null) {
                start_spawns.add(entity);
            } else if (team.team == Team.BLUE) {
                team_0_spawns.add(entity);
            } else if (team.team == Team.RED) {
                team_1_spawns.add(entity);
            }
        }
    }
    
    private void removeBalls() {
        for(Entity ball: balls) 
            engine.removeEntity(ball);
    }
    
    private PositionComponent getRandomSpawn(ArrayList<Entity> spawns) {
        int random = Math.round((float) Math.random() * (spawns.size() - 1));

        Entity spawn = spawns.get(random);
        return spawn.getComponent(PositionComponent.class);
    }
    
    private void resetBall(Team team) {
        removeBalls();
        PositionComponent pos;
        if(team == Team.BLUE)
            pos = getRandomSpawn(team_0_spawns);
        else if(team == Team.RED)
            pos = getRandomSpawn(team_1_spawns);
        else
            pos = getRandomSpawn(start_spawns);
        
        MapLoader.createEntity(engine, "ball", pos.x, pos.y, team);
    }

    @Override
    public void onChangeGameStateEvent(GameState newState) {
        if (newState == GameState.GAME)
            resetBall(null);
    }

    @Override
    public void onGoalEvent(Team team) {
        resetBall(team == Team.BLUE ? Team.RED : Team.BLUE);
    }
}
