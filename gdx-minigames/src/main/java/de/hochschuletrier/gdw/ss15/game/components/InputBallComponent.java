package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InputBallComponent extends Component implements Pool.Poolable {
	
	float horizontal;
	float vertical;
	
	boolean pull;
	boolean schieben;
	
	@Override
	public void reset() {
		this.horizontal = 0.0f;
		this.vertical = 0.0f;
		
		this.pull = false;
		this.schieben = false;
		
	}
	
}
