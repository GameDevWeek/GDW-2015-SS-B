package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerSpawnComponent extends Component implements Pool.Poolable {
	
	public long playerId = 0;
	
	@Override
	public void reset() {
		playerId = 0;
	}

	
}
