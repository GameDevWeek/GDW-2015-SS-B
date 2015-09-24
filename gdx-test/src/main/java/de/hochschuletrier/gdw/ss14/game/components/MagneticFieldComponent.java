package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MagneticFieldComponent extends Component implements Pool.Poolable {

	/*
	 * Wichtig: MagneticFieldComponent nur an Magnete hängen, die (physikalische) Sensoren sind
	 */
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
