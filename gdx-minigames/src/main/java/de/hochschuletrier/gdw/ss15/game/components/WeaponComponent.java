package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;



public class WeaponComponent extends Component implements Pool.Poolable{

	boolean isPulling;
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		isPulling=false;
	}

}
