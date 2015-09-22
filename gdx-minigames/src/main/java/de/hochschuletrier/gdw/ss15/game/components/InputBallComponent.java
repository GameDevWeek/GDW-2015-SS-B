package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InputBallComponent extends Component implements Pool.Poolable {
	
	public float horizontal;
	public float vertical;
	
	public boolean pull;
	public boolean push;
	
	@Override
	public void reset() {
		this.horizontal = 0.0f;
		this.vertical = 0.0f;
		
		this.pull = false;
		this.push = false;
		
	}
	
}
