package de.hochschuletrier.gdw.ss15.game.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.events.BallOwnershipChangedEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;
import java.util.Random;

public class PlayerSpawnManager {

    private final ImmutableArray<Entity> spawnPoints;
    private final PooledEngine engine;
    private final int teamCounts[] = new int[2];
    private final Random random = new Random();

    public PlayerSpawnManager(PooledEngine engine) {
        this.engine = engine;
        spawnPoints = engine.getEntitiesFor(Family.all(PlayerSpawnComponent.class, PositionComponent.class).get());
    }

    public Team getBestTeam() {
        if(teamCounts[0] > teamCounts[1])
            return Team.RED;
        else if(teamCounts[0] < teamCounts[1])
            return Team.BLUE;
        return Team.values()[random.nextInt(2)];
    }
    
    public Entity spawnPlayer() {
        Team chosenTeam = getBestTeam();
        
        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn.get(entity);
            if (spawn.playerId == 0) {
                PositionComponent pos = ComponentMappers.position.get(entity);
                TeamComponent team = ComponentMappers.team.get(entity);
                if(chosenTeam == team.team) {
                    teamCounts[chosenTeam.ordinal()]++;
                    Entity playerEntity = MapLoader.createEntity(engine, "player", pos.x, pos.y, chosenTeam);
                    spawn.playerId = playerEntity.getId();
                    return playerEntity;
                }
            }
        }
        return null;
    }

    public void freePlayer(long entityId) {
        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn.get(entity);
            TeamComponent team = ComponentMappers.team.get(entity);
            if (spawn.playerId == entityId) {
                teamCounts[team.team.ordinal()]--;
                spawn.playerId = 0;
                
                PlayerComponent player = ComponentMappers.player.get(entity);
                if(player.hasBall)
                    BallOwnershipChangedEvent.emit(null);
                engine.removeEntity(entity);
                return;
            }
        }
    }
}
