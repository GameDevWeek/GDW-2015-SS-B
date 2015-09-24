package de.hochschuletrier.gdw.ss15.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class PlayerSpawnManager {

    private final ImmutableArray<Entity> spawnPoints;
    private final PooledEngine engine;

    public PlayerSpawnManager(PooledEngine engine) {
        this.engine = engine;
        spawnPoints = engine.getEntitiesFor(Family.all(PlayerSpawnComponent.class, PositionComponent.class).get());
    }

    public Entity spawnPlayer() {
        //fixme: depending on team counts, otherwise random
        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn.get(entity);
            if (spawn.playerId == 0) {
                PositionComponent pos = ComponentMappers.position.get(entity);
                TeamComponent team = ComponentMappers.team.get(entity);
                Entity playerEntity = MapLoader.createEntity(engine, "player", pos.x, pos.y, team.team);
                spawn.playerId = playerEntity.getId();
                return playerEntity;
            }
        }
        return null;
    }

    public void freePlayer(long entityId) {
        for (Entity entity : spawnPoints) {
            PlayerSpawnComponent spawn = ComponentMappers.playerSpawn.get(entity);
            if (spawn.playerId == entityId) {
                spawn.playerId = 0;
                engine.removeEntity(entity);
                return;
            }
        }
    }
}
