package de.hochschuletrier.gdw.ss15.game.manager;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.data.GameState;;

public class BallManager implements ChangeGameStateEvent.Listener {
	
	ArrayList<Component> start_spawns = new ArrayList<Component>();
	ArrayList<Component> team_0_spawns = new ArrayList<Component>();
	ArrayList<Component> team_1_spawns = new ArrayList<Component>();
	
	public BallManager() {
		
		ChangeGameStateEvent.register(this);
		
//		for ( Component comp : ) {
//			
//		}
	}

	@Override
	public void onChangeGameStateEvent(GameState newState) {
		
		if (newState == GameState.GAME) {
			
		}
		
	}

}
