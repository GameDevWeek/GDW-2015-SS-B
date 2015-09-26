package de.hochschuletrier.gdw.ss15.game.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;
import de.hochschuletrier.gdw.ss15.events.*;

import java.util.ArrayList;
import java.util.Random;

public class PlayerSpawnManager implements GoalEvent.Listener,
        ChangeGameStateEvent.Listener {

    private final ImmutableArray<Entity> spawnPoints;
    // private final ArrayList<Entity> players;
    private final ImmutableArray<Entity> players;
    private final PooledEngine engine;
    private final int teamCounts[] = new int[2];
    private final Random random = new Random();

    public PlayerSpawnManager(PooledEngine engine) {
        this.engine = engine;
        spawnPoints = engine.getEntitiesFor(Family.all(
                PlayerSpawnComponent.class, PositionComponent.class).get());
        players = engine
                .getEntitiesFor(Family.all(PlayerComponent.class).get());
        ChangeGameStateEvent.register(this);
        GoalEvent.register(this);
    }
    
    public void dispose(){
        ChangeGameStateEvent.unregister(this);
        GoalEvent.unregister(this);
        
    }

    public Team getBestTeam() {
        if (teamCounts[0] > teamCounts[1])
            return Team.RED;
        else if (teamCounts[0] < teamCounts[1])
            return Team.BLUE;
        return Team.values()[random.nextInt(2)];
    }

    public Entity spawnPlayer() {
        Team chosenTeam = getBestTeam();
        System.out.println("spawn PLayer");

        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn
                    .get(entity);
            if (spawn.playerId == 0) {
                PositionComponent pos = ComponentMappers.position.get(entity);
                TeamComponent team = ComponentMappers.team.get(entity);
                if (chosenTeam == team.team) {
                    teamCounts[chosenTeam.ordinal()]++;
                    Entity playerEntity = MapLoader.createEntity(engine,
                            "player", pos.x, pos.y, chosenTeam);
                    spawn.playerId = playerEntity.getId();
                    System.out.println("player " + players.size());
                    return playerEntity;
                }
            }
        }
        return null;
    }

    
    public Entity getPlayerSpawn(long playerId) {
        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn
                    .get(entity);
            if (spawn.playerId == playerId) {
                return entity;
            }
        }
        return null;
    }
    
    public void freePlayer(Entity playerEntity) {
        Entity spawnEntity = getPlayerSpawn(playerEntity.getId());
        if(spawnEntity != null) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn.get(spawnEntity);
            TeamComponent team = ComponentMappers.team.get(spawnEntity);
            teamCounts[team.team.ordinal()]--;
            spawn.playerId = 0;

            PlayerComponent player = ComponentMappers.player
                    .get(playerEntity);
            if (player.hasBall)
                BallDropEvent.emit(playerEntity, Vector2.Zero);

        }
        engine.removeEntity(playerEntity);
    }

    public void resetPlayers(boolean isStunning) {
        System.out.println("reset");
        for (Entity player : players) {
            Entity spawnEntity = getPlayerSpawn(player.getId());
            if(spawnEntity != null) {
                PhysixBodyComponent physixBody=player.getComponent(PhysixBodyComponent.class);
                physixBody.setX(spawnEntity.getComponent(PositionComponent.class).x);
                physixBody.setY(spawnEntity.getComponent(PositionComponent.class).y);
                physixBody.setLinearVelocity(0,0);
                player.getComponent(InputBallComponent.class).isStunned=isStunning;
            }
        }
    }

    @Override
    public void onGoalEvent(Team team) {
        // TODO Auto-generated method stub
        resetPlayers(false);

    }

    @Override
    public void onChangeGameStateEvent(GameState newState, float gameTime) {
        // TODO Auto-generated method stub

        switch(newState){
        case THREE:
            resetPlayers(true);
            break;
        case GAME:
            resetPlayers(false);
            break;
        }

    }

}
