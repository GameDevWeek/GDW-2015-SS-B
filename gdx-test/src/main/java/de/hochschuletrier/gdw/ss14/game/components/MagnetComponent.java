package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MagnetComponent extends Component implements Pool.Poolable {

	public boolean positiv;
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		positiv = true;
	}

}
