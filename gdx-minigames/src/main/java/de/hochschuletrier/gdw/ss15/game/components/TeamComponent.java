package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.data.Team;

public class TeamComponent extends Component implements Pool.Poolable {

	public Team team;
	
	@Override
	public void reset() {
		team = null;
	}
	
	
	
}
