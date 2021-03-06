package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MagneticFieldComponent extends Component implements Pool.Poolable {

	/*
	 * Wichtig: MagneticFieldComponent nur an Magnete hängen, die (physikalische) Sensoren sind
	 */
	public float range;
        public float stateTime;
    
	@Override
	public void reset() {
	    range=0;
            stateTime = 0;
	}

}
