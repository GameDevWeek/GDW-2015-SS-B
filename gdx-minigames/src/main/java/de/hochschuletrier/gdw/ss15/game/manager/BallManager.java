package de.hochschuletrier.gdw.ss15.game.manager;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.components.BallSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;;

public class BallManager implements ChangeGameStateEvent.Listener, GoalEvent.Listener {
	
	ArrayList<Entity> start_spawns = new ArrayList<Entity>();
	ArrayList<Entity> team_0_spawns = new ArrayList<Entity>();
	ArrayList<Entity> team_1_spawns = new ArrayList<Entity>();
	
	private PooledEngine engine;
	
	public BallManager(PooledEngine engine) {
		this.engine = engine;
		
		distributeSpawns();
		
		ChangeGameStateEvent.register(this);
		GoalEvent.register(this);
		
	}
	
	public void distributeSpawns() {
	    
	    ImmutableArray<Entity> spawns = engine.getEntitiesFor(Family.all(BallSpawnComponent.class, TeamComponent.class).get());
	    
	    for (Entity entity : spawns) {
	        TeamComponent team = entity.getComponent(TeamComponent.class);
	        
	        if( team.team == null) {
                start_spawns.add(entity);
            }
	        else if(team.team == Team.BLUE) {
	            team_0_spawns.add(entity);
	        }
	        else if(team.team == Team.RED) {
	            team_1_spawns.add(entity);
	        }
        }
	}

	@Override
	public void onChangeGameStateEvent(GameState newState) {

		if (newState == GameState.GAME) {
		    int random = Math.round((float)Math.random() * (start_spawns.size() - 1));
		    
		    Entity spawn = start_spawns.get(random);
		    PositionComponent pos = spawn.getComponent(PositionComponent.class);
		    
		    MapLoader.createEntity(engine, "ball", pos.x, pos.y, null);
		}
	}

    @Override
    public void goal(Team team) {
        int random;
        Entity spawn;
        Team spawnTeam;
        
        if(team != Team.BLUE) {
            random = Math.round((float)Math.random() * (team_0_spawns.size() - 1));
            spawn = team_0_spawns.get(random);
            spawnTeam = Team.BLUE;
        }
        else {
            random = Math.round((float)Math.random() * (team_1_spawns.size() - 1));
            spawn = team_1_spawns.get(random);
            spawnTeam = Team.RED;
        }
         
        PositionComponent pos = spawn.getComponent(PositionComponent.class);
        
        MapLoader.createEntity(engine, "ball", pos.x, pos.y, spawnTeam);
    }

}
