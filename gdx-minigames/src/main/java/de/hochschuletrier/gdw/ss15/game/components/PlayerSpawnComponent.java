package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerSpawnComponent extends Component implements Pool.Poolable {
	
	/*
	 * false = nicht besetzt
	 * true  = besetzt
	 */
	public boolean busy = false;
	
	@Override
	public void reset() {
		this.busy = false;
	}

	
}
